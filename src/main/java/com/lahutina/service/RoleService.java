package com.lahutina.service;

import com.lahutina.model.Role;
import java.util.List;

public interface RoleService {
    Role create(Role role);
    Role update(Role role);
    void delete(long id);
    List<Role> getAll();
}
