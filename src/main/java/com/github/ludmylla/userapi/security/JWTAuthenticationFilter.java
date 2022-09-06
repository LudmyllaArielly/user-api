package com.github.ludmylla.userapi.security;

import com.github.ludmylla.userapi.domain.service.impl.UserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.ludmylla.userapi.security.Constants.HEADER_STRING;
import static com.github.ludmylla.userapi.security.Constants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");

            try {
                username = tokenProvider.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException ex) {
                logger.info("There was an error getting the username from the token.");

            } catch (ExpiredJwtException ex) {
                logger.info("A bearer was not found or the header was ignored.");

            } catch (SignatureException ex) {
                logger.info("Authentication failed. Username or password not valid.");


            } catch (MalformedJwtException ex) {
                logger.info("Invalid JWT token.");


            } catch (UnsupportedJwtException ex) {
                logger.info("Unsupported JWT token.");
            }
        } else {
            logger.info("A bearer was not found or the header was ignored.");
        }

        getAuthentication(username, authToken, request);

        filterChain.doFilter(request, response);
    }


    private void getAuthentication(String username, String authToken, HttpServletRequest request) {

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userServiceImpl.loadUserByUsername(username);

            if (tokenProvider.validateToken(authToken, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken
                        = tokenProvider.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user" + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }
    }
}
