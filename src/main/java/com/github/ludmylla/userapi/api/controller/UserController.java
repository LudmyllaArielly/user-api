package com.github.ludmylla.userapi.api.controller;

import com.github.ludmylla.userapi.api.assembler.UserModelAssembler;
import com.github.ludmylla.userapi.api.disassembler.UserInputDisassembler;
import com.github.ludmylla.userapi.domain.dto.AuthToken;
import com.github.ludmylla.userapi.domain.dto.LoginDTO;
import com.github.ludmylla.userapi.domain.dto.UserModel;
import com.github.ludmylla.userapi.domain.dto.input.UserInput;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.service.UserService;
import com.github.ludmylla.userapi.domain.service.exceptions.UserBadCredentialsException;
import com.github.ludmylla.userapi.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private UserModelAssembler userModelAssembler;

    @Autowired
    private UserInputDisassembler userInputDisassembler;

    @PostMapping("/signUp")
    public ResponseEntity<UserModel> create(@RequestBody @Valid UserInput userInput) {
        User user = userInputDisassembler.toDomainModel(userInput);
        user = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModelAssembler.toModel(user));
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthToken(token));

        } catch (BadCredentialsException ex) {
            throw new UserBadCredentialsException("Authentication failed. Username or password not valid.");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> findAll() {
        List<User> list = userService.findAll();
        return ResponseEntity.ok(userModelAssembler.toCollectionModel(list));

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    @GetMapping("/findEmail")
    public ResponseEntity<UserModel> findByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> update(@PathVariable Long id, @RequestBody @Valid UserInput userInput) {
        User user = userInputDisassembler.toDomainModel(userInput);
        user = userService.update(id, user);
        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
