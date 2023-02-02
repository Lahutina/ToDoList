package com.lahutina.service;

import com.lahutina.model.ToDo;
import java.util.List;

public interface ToDoService {
    ToDo create(ToDo todo);
    ToDo readById(long todoId);
    ToDo update(ToDo todo);
    void delete(long id);
    List<ToDo> getAll();
    List<ToDo> getByUserId(long userId);
}
