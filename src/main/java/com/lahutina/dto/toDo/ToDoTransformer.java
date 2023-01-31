package com.lahutina.dto.toDo;

import com.lahutina.model.ToDo;
import com.lahutina.model.User;

public class ToDoTransformer {
    public static ToDoDto convertToDto(ToDo toDo)
    {
        ToDoDto toDoDto = new ToDoDto();
        toDoDto.setId(toDo.getId());
        toDoDto.setTitle(toDo.getTitle());
        toDoDto.setCreatedAt(toDo.getCreatedAt());
        toDoDto.setOwnerId(toDo.getOwner().getId());

        return toDoDto;
    }

    public static ToDo convertToEntity(ToDoDto toDoDto, User owner){
        ToDo toDo = new ToDo();
        toDo.setTitle(toDoDto.getTitle());
        toDo.setOwner(owner);
        toDo.setCreatedAt(toDoDto.getCreatedAt());
        return toDo;
    }
}

