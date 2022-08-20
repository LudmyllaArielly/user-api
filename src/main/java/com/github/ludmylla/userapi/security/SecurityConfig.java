package com.github.ludmylla.userapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/users/signUp/**").permitAll()
                    .antMatchers(HttpMethod.POST,"/users/signIn/**").permitAll()
                    .antMatchers(HttpMethod.GET,"/users/{id}/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                    .antMatchers(HttpMethod.GET,"/users/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                    .antMatchers(HttpMethod.GET,"/users/findEmail/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                    .antMatchers(HttpMethod.PUT,"/users/{id}/**").hasAnyRole("ROLE_ADMIN")
                    .antMatchers(HttpMethod.DELETE,"/users/{id}/**").hasAnyRole("ROLE_ADMIN")
                    .anyRequest().authenticated()

                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors().and().csrf().disable();

        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public JWTAuthenticationFilter authenticationFilter() throws Exception{
        return new JWTAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
