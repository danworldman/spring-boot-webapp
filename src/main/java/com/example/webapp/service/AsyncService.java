package com.example.webapp.service;

import com.example.webapp.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
public class AsyncService {

    @Async // метод выполнится в отдельном виртуальном потоке
    // реагирует на публикацию UserRegisteredEvent
    // @TransactionalEventListener — гарантирует, что задача начнется только после сохранения записи о пользователе в БД
    // TransactionPhase.AFTER_COMMIT — если в UserService случится ошибка, транзакция откатится, и метод не запустится
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistration(UserRegisteredEvent event) {
        // Достаем username из рекорда события
        String username = event.username();

        log.info("Событие. Начало фоновой задачи для юзера: {}. Поток: {}",
                username, Thread.currentThread());

        try {
            Thread.sleep(3000); // имитация тяжелой операции
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Фоновая задача по событию завершена для юзера: {}", username);
    }
}