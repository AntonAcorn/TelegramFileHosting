package ru.acorn.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumeFromRabbit {
    void consumeTextMessage (Update update);
    void consumeDocMessage (Update update);
    void consumePhotoMessage (Update update);
}
