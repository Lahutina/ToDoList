package com.lahutina.controller;

import com.lahutina.config.JwtProvider;
import com.lahutina.dto.OperationResponse;
import com.lahutina.dto.auth.AuthRequestDto;
import com.lahutina.dto.auth.AuthResponseDto;
import com.lahutina.dto.user.UserDto;
import com.lahutina.dto.user.UserTransformer;
import com.lahutina.model.User;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    @Autowired
    private final UserService userService;


    @PostMapping(value={"/", "/signin"})
    public AuthResponseDto signIn(@RequestBody @Valid AuthRequestDto authRequest) {
        User user = userService.findByLogin(authRequest.getLogin());
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(),
                authRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        return new AuthResponseDto(jwtProvider.generateToken(user));
    }

    @PostMapping("/signup")
    public OperationResponse signUp(@RequestBody UserDto userDto) {
        User user = userService.saveUser(UserTransformer.convertToEntity(userDto));
        if(user!=null)
            return new OperationResponse(String.valueOf(true));
        else  return new OperationResponse(String.valueOf(false));
    }
}
