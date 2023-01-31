package com.lahutina.service;

import com.lahutina.model.Task;
import java.util.List;

public interface TaskService {
    Task create(Task task);
    Task update(Task task);
    void delete(long id);
    List<Task> getAll();
    List<Task> getByTodoId(long todoId);
    Task readById(long id);
}
