package com.lahutina.service.impl;

import com.lahutina.exception.NullEntityReferenceException;
import com.lahutina.model.State;
import com.lahutina.repository.StateRepository;
import com.lahutina.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class StateServiceImpl implements StateService {
    private final StateRepository stateRepository;

    @Override
    public State create(State state) {
        if (state != null) {
            return stateRepository.save(state);
        } else
            throw new NullEntityReferenceException("State cannot be 'null'");
    }

    @Override
    public State update(State state) {
        if (state != null) {
            return stateRepository.save(state);
        } else
            throw new NullEntityReferenceException("State cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Optional<State> stateOptional = stateRepository.findById(id);

        if (stateOptional.isPresent()) {
            stateRepository.delete(stateOptional.get());
        } else
            throw new EntityNotFoundException("State with id " + id + " not found");
    }

    @Override
    public State getByName(String name) {
        return stateRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("State with name '" + name + "' not found"));
    }

    @Override
    public List<State> getAll() {
        List<State> states = stateRepository.getAll();
        return states.isEmpty() ? new ArrayList<>() : states;
    }
}
