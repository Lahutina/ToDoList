package com.lahutina.controller;

import com.lahutina.dto.todo.ToDoDto;
import com.lahutina.dto.user.UserDto;
import com.lahutina.model.ToDo;
import com.lahutina.model.User;
import com.lahutina.service.ToDoService;
import com.lahutina.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoService todoService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ToDoDto>> getAll() {
        long ownerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ToDoDto> toDos = todoService.getByUserId(ownerId)
                .stream()
                .map(t -> modelMapper.map(t, ToDoDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(toDos.isEmpty() ? new ArrayList<>() : toDos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#id) >= 1")
    public ResponseEntity<ToDoDto> read(@PathVariable long id) {
        ToDoDto toDoDto = modelMapper.map(todoService.readById(id), ToDoDto.class);

        return new ResponseEntity<>(toDoDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#id) == 1")
    public ResponseEntity<ToDoDto> update(@PathVariable long id, @RequestBody ToDoDto toDoDto) {

        ToDo toDo = todoService.readById(id);
        toDo.setTitle(toDoDto.getTitle());
        todoService.update(toDo);

        return new ResponseEntity<>(modelMapper.map(todoService.readById(id), ToDoDto.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ToDoDto> create(@Validated @RequestBody ToDoDto toDoDto) {
        long ownerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        toDoDto.setCreatedAt(LocalDateTime.now());
        toDoDto.setOwnerId(ownerId);

        ToDo toDo = modelMapper.map(toDoDto, ToDo.class);
        toDo = todoService.create(toDo);

        return new ResponseEntity<>(modelMapper.map(toDo, ToDoDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or  @toDoController.isOwnerOrCollaborator(#id) == 1")
    public ResponseEntity<?> delete(@PathVariable long id) {
        todoService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{todo_id}/collaborators")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<List<UserDto>> getAllCollaborators(@PathVariable("todo_id") long todoId) {
        ToDo todo = todoService.readById(todoId);

        List<UserDto> collaborators = todo.getCollaborators()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(collaborators.isEmpty() ? new ArrayList<>() : collaborators, HttpStatus.OK);
    }

    @PostMapping("/{todo_id}/collaborators/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or  @toDoController.isOwnerOrCollaborator(#toDoId) == 1")
    public ResponseEntity<?> addCollaborator(@PathVariable("todo_id") long toDoId,
                                             @PathVariable long id) {
        ToDo todo = todoService.readById(toDoId);
        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(id));
        todo.setCollaborators(collaborators);
        todoService.update(todo);

        List<UserDto> collabDto = collaborators
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(collabDto.isEmpty() ? new ArrayList<>() : collabDto, HttpStatus.OK);
    }

    @DeleteMapping("/{todo_id}/collaborators/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or  @toDoController.isOwnerOrCollaborator(#toDoId) == 1")
    public ResponseEntity<?> removeCollaborator(@PathVariable("todo_id") long toDoId,
                                                @PathVariable long id) {
        ToDo todo = todoService.readById(toDoId);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(id));
        todo.setCollaborators(collaborators);
        todoService.update(todo);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public int isOwnerOrCollaborator(long todoId) {

        long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ToDo toDo = todoService.readById(todoId);

        if (toDo.getOwner().getId() == currentUserId) {
            return 1;
        }
        for (User user : toDo.getCollaborators()) {
            if (user.getId() == currentUserId) {
                return 2;
            }
        }
        return 0;
    }
}