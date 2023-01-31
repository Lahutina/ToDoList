package com.lahutina.service;

import com.lahutina.model.State;
import java.util.List;
import java.util.Optional;

public interface StateService {
    State create(State state);
    State update(State state);
    void delete(long id);
    State getByName(String name);
    List<State> getAll();
}
