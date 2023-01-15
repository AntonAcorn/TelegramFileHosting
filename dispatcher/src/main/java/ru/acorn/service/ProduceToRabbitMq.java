package ru.acorn.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProduceToRabbitMq {
    void produce(String rabbitQueue, Update update);
}
