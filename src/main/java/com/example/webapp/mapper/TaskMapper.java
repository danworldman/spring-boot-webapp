package com.example.webapp.mapper;

import com.example.webapp.dto.task.CreateTaskRequestDTO;
import com.example.webapp.dto.task.TaskResponseDTO;
import com.example.webapp.entity.Task;
import com.example.webapp.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequestDTO dto){
        if (dto == null) return null;
        Task task = new Task();
        task.setTitle(dto.title());
        task.setText(dto.text());
        return task;
    }

    public TaskResponseDTO toDto(Task task){
        if (task == null) return null;

        Long userId = Optional.ofNullable(task.getUser())
                .map(User::getId)
                .orElse(null);

        return new TaskResponseDTO(
                task.getId(),
                userId,
                task.getTitle(),
                task.getText()
        );
    }
}