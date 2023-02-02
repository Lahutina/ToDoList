package com.lahutina.service.impl;

import com.lahutina.exception.NullEntityReferenceException;
import com.lahutina.model.Role;
import com.lahutina.repository.RoleRepository;
import com.lahutina.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role create(Role role) {
        if (role != null) {
            return roleRepository.save(role);
        } else
            throw new NullEntityReferenceException("Role cannot be 'null'");
    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Role with id " + id + " not found"));
    }

    @Override
    public Role update(Role role) {
        if (role != null) {
            return roleRepository.save(role);
        } else
            throw new NullEntityReferenceException("Role cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isPresent()) {
            roleRepository.delete(roleOptional.get());
        } else
            throw new EntityNotFoundException("Role with id " + id + " not found");
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }
}
