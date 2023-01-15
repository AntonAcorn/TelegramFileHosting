package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.acorn.controller.UpdateController;
import ru.acorn.service.ConsumeFromRabbit;

import static ru.acorn.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@Log4j
public class ConsumeFromRabbitImpl implements ConsumeFromRabbit {
    private final UpdateController updateController;

    public ConsumeFromRabbitImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        log.debug(sendMessage.getText());
        updateController.setView(sendMessage);
    }
}
