package com.lahutina.controller;

import com.lahutina.dto.toDo.ToDoDto;
import com.lahutina.dto.user.UserDto;
import com.lahutina.model.Role;
import com.lahutina.model.User;
import com.lahutina.service.RoleService;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<List<String>> listRoles() {
        List<String> roles = roleService.getAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new ResponseEntity<>(roles, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> users = userService.getAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(users.isEmpty() ? new ArrayList<>() : users, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') || principal == #id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> read(@PathVariable long id) throws EntityNotFoundException {
        UserDto user = modelMapper.map(userService.readById(id), UserDto.class);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        User userCreated = userService.create(modelMapper.map(userDto, User.class));

        return new ResponseEntity<>(modelMapper.map(userCreated, UserDto.class), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') || principal == #id")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        User user = userService.readById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userService.update(user);

        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') || principal == #id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) throws EntityNotFoundException {
        userService.delete(id);
    }
}
