package ru.acorn.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumeFromRabbitMq {
    void consume(SendMessage sendMessage);
}
