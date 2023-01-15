package ru.acorn.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.acorn.service.ProduceFromNode;

import static ru.acorn.model.RabbitQueue.ANSWER_MESSAGE;

@Service
public class ProduceFromNodeImpl implements ProduceFromNode {
    private final RabbitTemplate rabbitTemplate;

    public ProduceFromNodeImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}
