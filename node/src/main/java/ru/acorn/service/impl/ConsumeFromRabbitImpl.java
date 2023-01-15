package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.service.ConsumeFromRabbit;
import ru.acorn.service.ProduceFromNode;
import ru.acorn.utils.NodeMessageUtils;

import static ru.acorn.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumeFromRabbitImpl implements ConsumeFromRabbit {
    private final ProduceFromNode produceFromNode;
    private final NodeMessageUtils nodeMessageUtils;

    public ConsumeFromRabbitImpl(ProduceFromNode produceFromNode, NodeMessageUtils nodeMessageUtils) {
        this.produceFromNode = produceFromNode;
        this.nodeMessageUtils = nodeMessageUtils;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessage(Update update) {
        log.debug(update.getMessage().getText());
        var msg = nodeMessageUtils.generateAnswer(update, "Hello from Node");
        produceFromNode.sendAnswer(msg);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessage(Update update) {

    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessage(Update update) {

    }
}
