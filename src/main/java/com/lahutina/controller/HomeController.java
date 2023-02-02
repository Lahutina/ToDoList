package com.lahutina.controller;

import com.lahutina.dto.user.UserDto;
import com.lahutina.model.User;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping({"/", "home"})
    public List<UserDto> getAll() {
        List<UserDto> users = userService.getAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
        return users.isEmpty() ? new ArrayList<>() : users;
    }
}
