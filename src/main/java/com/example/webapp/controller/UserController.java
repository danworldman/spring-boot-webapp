package com.example.webapp.controller;

import com.example.webapp.dto.user.CreateUserRequestDto;
import com.example.webapp.dto.user.UpdateUserRequestDto;
import com.example.webapp.dto.user.UserResponseDto;
import com.example.webapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Validated // проверка параметров в методах
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable @Min(value = 1, message = "ID должен быть больше 0") Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<UserResponseDto> getAll(
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return service.getAll(pageable);
    }

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody CreateUserRequestDto dto) {
        return service.create(dto);
    }


    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateUserRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(1) Long id) {
        service.delete(id);
    }
}