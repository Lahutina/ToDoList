package com.lahutina.service.impl;

import com.lahutina.config.CustomUserDetails;
import com.lahutina.dto.auth.AuthRequestDto;
import com.lahutina.exception.IncorrectPasswordException;
import com.lahutina.exception.NullEntityReferenceException;
import com.lahutina.model.Role;
import com.lahutina.model.User;
import com.lahutina.repository.RoleRepository;
import com.lahutina.repository.UserRepository;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        if (user == null) {
            throw new NullEntityReferenceException("User cannot be 'null'");
        } else if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is occupied");
        }

        user.setRole(roleRepository.findByName("User"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else
            throw new EntityNotFoundException("User with login " + login + " not found");
    }

    @Override
    public User findByLoginAndPassword(AuthRequestDto authRequest) {
        Optional<User> userOptional = userRepository.findByLogin(authRequest.getLogin());

        if (userOptional.isEmpty()) {
            throw new NullEntityReferenceException("User is null");
        } else if (!passwordEncoder.matches(authRequest.getPassword(),
                userOptional.get().getPassword())) {
            throw new IncorrectPasswordException("Password for user with login " + authRequest.getLogin() + " not correct");
        } else
            return userOptional.get();
    }

    @Override
    public String getExpirationLocalDate() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime localDate = customUserDetails.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' hh:mm");
        return localDate.format(formatter);
    }

    @Override
    public User update(User user) {
        if (user != null) {
            return userRepository.save(user);
        } else
            throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else
            throw new EntityNotFoundException("ToDo with id " + id + " not found");
    }

    public List<Role> getAllUserRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User with id " + id + " not found"));
    }
}
