package com.lahutina.service.impl;

import com.lahutina.exception.NullEntityReferenceException;
import com.lahutina.model.Task;
import com.lahutina.repository.TaskRepository;
import com.lahutina.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Task create(Task task) {
        if (task != null) {
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public Task update(Task task) {
        if (task != null) {
            return taskRepository.save(task);
        } else
            throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
        } else
            throw new EntityNotFoundException("Task with id " + id + " not found");
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> tasks = taskRepository.getByTodoId(todoId);
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Task with id " + id + " not found"));
    }
}
