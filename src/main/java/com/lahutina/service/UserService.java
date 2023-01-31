package com.lahutina.service;

import com.lahutina.dto.auth.AuthRequestDto;
import com.lahutina.model.Role;
import com.lahutina.model.User;
import java.util.List;

public interface UserService {
    User create(User user);
    User findByLogin(String login);
    User findByLoginAndPassword(AuthRequestDto authRequest);
    String getExpirationLocalDate();
    List<Role> getAllUserRoles();
    User update(User user);
    void delete(long id);
    List<User> getAll();
    User readById(long id);
}
