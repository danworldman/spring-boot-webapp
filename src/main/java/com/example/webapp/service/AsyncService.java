package com.example.webapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    @Async // этот метод выполнится в отдельном потоке
    public void processExternalTask(String username) {
        log.info("Начало фоновой задачи для юзера: {}. Поток: {}", username, Thread.currentThread());
        try {
            Thread.sleep(3000); // имитация тяжелой операции
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Фоновая задача завершена для юзера: {}", username);
    }
}