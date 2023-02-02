package com.lahutina.controller;

import com.lahutina.dto.task.TaskDto;
import com.lahutina.model.Priority;
import com.lahutina.model.Task;
import com.lahutina.service.StateService;
import com.lahutina.service.TaskService;
import com.lahutina.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{todo_id}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<List<TaskDto>> getAll(@PathVariable("todo_id") long todoId) {

        List<TaskDto> taskDtos = todoService.readById(todoId).getTasks()
                .stream()
                .map(t -> modelMapper.map(t, TaskDto.class))
                .collect(Collectors.toList());

        taskDtos
                .forEach(u->u.setState(taskService.readById(u.getId()).getState().getName()));

        return new ResponseEntity<>(taskDtos.isEmpty() ? new ArrayList<>() : taskDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<TaskDto> read(@PathVariable("todo_id") long todoId,
                                        @PathVariable long id) {
        TaskDto taskDto = modelMapper.map(taskService.readById(id), TaskDto.class);
        taskDto.setState(taskService.readById(id).getState().getName());

        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<TaskDto> create(@PathVariable("todo_id") long todoId,
                                          @RequestBody TaskDto taskDto) {

        taskDto.setTodoId(todoId);
        Task task = modelMapper.map(taskDto, Task.class);
        task.setState(stateService.getByName(taskDto.getState()));

        task = taskService.create(task);

        taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setState(task.getState().getName());

        return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<TaskDto> update(@PathVariable long id,
                                          @PathVariable("todo_id") long todoId,
                                          @RequestBody TaskDto taskDto) {
        Task task = taskService.readById(id);

        task.setName(taskDto.getName());
        task.setState(stateService.getByName(taskDto.getState()));
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        taskService.update(task);

        taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setState(task.getState().getName());

        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<?> delete(@PathVariable("todo_id") long todoId, @PathVariable long id) {
        taskService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
