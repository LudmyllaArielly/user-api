package com.github.ludmylla.userapi.api.controller;

import com.github.ludmylla.userapi.domain.dto.AuthToken;
import com.github.ludmylla.userapi.domain.dto.LoginDTO;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.service.UserService;
import com.github.ludmylla.userapi.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/signUp")
    public ResponseEntity<User> create(@RequestBody User user){
        User userSaved = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO)  {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthToken(token));

        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        List<User> list = userService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findEmail")
    public ResponseEntity<User> findByEmail(@RequestParam String email){
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update (@PathVariable Long id, @RequestBody User user){
        User userUpdated = userService.update(id, user);
        return ResponseEntity.ok(userUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
