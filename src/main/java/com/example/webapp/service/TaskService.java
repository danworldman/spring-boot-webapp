package com.example.webapp.service;

import com.example.webapp.dto.task.CreateTaskRequestDTO;
import com.example.webapp.dto.task.TaskResponseDTO;
import com.example.webapp.entity.Task;
import com.example.webapp.entity.User;
import com.example.webapp.exception.ResourceNotFoundException;
import com.example.webapp.mapper.TaskMapper;
import com.example.webapp.repository.TaskRepository;
import com.example.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository; // для юзера
    private final TaskMapper mapper;

    public List<TaskResponseDTO> getAll(){
        return taskRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public TaskResponseDTO create(CreateTaskRequestDTO dto, String username) {

        // поиск теперь по логину, а не по id
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь " + username + " не найден"));

        // мапим DTO там теперь только title и text
        Task entity = mapper.toEntity(dto);

        // привязка найденного юзера из БД
        entity.setUser(user);

        Task saved = taskRepository.save(entity);
        return mapper.toDto(saved);
    }
}