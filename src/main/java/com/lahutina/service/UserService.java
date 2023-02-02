package com.lahutina.service;

import com.lahutina.dto.auth.AuthRequestDto;
import com.lahutina.model.User;
import java.util.List;

public interface UserService {
    User create(User user);
    User readById(long id);
    User update(User user);
    void delete(long id);
    User findByLoginAndPassword(AuthRequestDto authRequest);
    List<User> getAll();
}
