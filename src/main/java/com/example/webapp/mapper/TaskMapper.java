package com.example.webapp.mapper;

import com.example.webapp.dto.task.CreateTaskRequestDto;
import com.example.webapp.dto.task.TaskResponseDto;
import com.example.webapp.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequestDto dto){
        Task task = new Task();
        task.setTitle(dto.title());
        task.setText(dto.text());
        return task;
    }

    public TaskResponseDto toDto(Task task){
        if (task == null) return null;
        return new TaskResponseDto(
                task.getId(),
                task.getUser().getId(),
                task.getTitle(),
                task.getText()
        );
    }
}