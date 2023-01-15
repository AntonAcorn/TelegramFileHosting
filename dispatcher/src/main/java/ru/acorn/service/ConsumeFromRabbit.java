package ru.acorn.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ConsumeFromRabbit {
    void consume(SendMessage sendMessage);
}
