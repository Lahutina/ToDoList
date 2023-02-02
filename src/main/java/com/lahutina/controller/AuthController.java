package com.lahutina.controller;

import com.lahutina.config.JwtProvider;
import com.lahutina.dto.auth.AuthRequestDto;
import com.lahutina.dto.auth.AuthResponseDto;
import com.lahutina.dto.user.UserDto;
import com.lahutina.model.User;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping(value = {"/", "/signin"})
    public AuthResponseDto signIn(@RequestBody @Valid AuthRequestDto authRequest) {
        User user = userService.findByLoginAndPassword(authRequest);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(),
                authRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        return new AuthResponseDto(jwtProvider.generateToken(user));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid UserDto userDto) {
        User userCreated = userService.create(modelMapper.map(userDto, User.class));

        return userCreated != null ? new ResponseEntity<>(modelMapper.map(userCreated, UserDto.class), HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
