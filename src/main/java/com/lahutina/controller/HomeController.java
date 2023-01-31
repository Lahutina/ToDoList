package com.lahutina.controller;

import com.lahutina.dto.user.UserDto;
import com.lahutina.dto.user.UserTransformer;
import com.lahutina.model.User;
import com.lahutina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping({"/", "home"})
    public List<UserDto> getAll() {
        List<UserDto> users= new ArrayList<>();
        for(User user:userService.getAll())
            users.add(UserTransformer.convertToDto(user));
        return users;
    }
}
