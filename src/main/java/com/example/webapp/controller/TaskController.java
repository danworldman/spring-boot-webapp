package com.example.webapp.controller;

import com.example.webapp.dto.task.CreateTaskRequestDTO;
import com.example.webapp.dto.task.TaskResponseDTO;
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
    public List<TaskResponseDTO> getAll(){
        return service.getAll();
    }

    @PostMapping
    public TaskResponseDTO create(@RequestBody CreateTaskRequestDTO dto, Principal principal) {
        // Вытаскиваем имя залогиненного юзера и отдаем в сервис
        return service.create(dto, principal.getName());
    }
}