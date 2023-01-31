package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.acorn.controller.UpdateProcessor;
import ru.acorn.service.ConsumeFromRabbit;

import static ru.acorn.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@Log4j
public class ConsumeFromRabbitImpl implements ConsumeFromRabbit {
    private final UpdateProcessor updateProcessor;

    public ConsumeFromRabbitImpl(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        log.debug(sendMessage.getText());
        updateProcessor.setView(sendMessage);
    }
}
