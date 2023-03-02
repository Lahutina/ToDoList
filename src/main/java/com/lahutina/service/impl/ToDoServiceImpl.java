package com.lahutina.service.impl;

import com.lahutina.exception.NullEntityReferenceException;
import com.lahutina.model.ToDo;
import com.lahutina.repository.ToDoRepository;
import com.lahutina.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;

    @Override
    public ToDo create(ToDo role) {
        if (role != null) {
            return todoRepository.save(role);
        } else
            throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public ToDo update(ToDo toDo) {
        if (toDo != null) {
            return todoRepository.save(toDo);
        } else
            throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Optional<ToDo> toDoOptional = todoRepository.findById(id);

        if (toDoOptional.isPresent()) {
            todoRepository.delete(toDoOptional.get());
        } else
            throw new EntityNotFoundException("ToDo with id " + id + " not found");
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> todos = todoRepository.findAll();
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = todoRepository.getByUserId(userId);
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public ToDo readById(long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(()-> new EntityNotFoundException("ToDo with id " + todoId + " not found"));
    }
}
