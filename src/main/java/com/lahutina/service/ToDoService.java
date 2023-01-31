package com.lahutina.service;

import com.lahutina.model.ToDo;
import java.util.List;

public interface ToDoService {
    ToDo create(ToDo todo);
    ToDo update(ToDo todo);
    void delete(long id);
    List<ToDo> getAll();
    List<ToDo> getByUserId(long userId);
    ToDo readById(long todoId);
}
