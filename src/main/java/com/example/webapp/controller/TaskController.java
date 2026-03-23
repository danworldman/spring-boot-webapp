package com.example.webapp.controller;

import com.example.webapp.dto.task.CreateTaskRequestDto;
import com.example.webapp.dto.task.TaskResponseDto;
import com.example.webapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/tasks")
public class TaskController {

    private final TaskService service;

    @GetMapping
    public List<TaskResponseDto> getAll(){
        return service.getAll();
    }

    @PostMapping
    public TaskResponseDto create(@RequestBody CreateTaskRequestDto dto, Principal principal) {
        // Вытаскиваем имя залогиненного юзера и отдаем в сервис
        return service.create(dto, principal.getName());
    }
}