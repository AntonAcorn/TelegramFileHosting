package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.service.ConsumeFromRabbit;
import ru.acorn.service.MainService;
import ru.acorn.service.ProduceFromNode;
import ru.acorn.utils.NodeMessageUtils;

import static ru.acorn.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumeFromRabbitImpl implements ConsumeFromRabbit {
    private final MainService mainService;

    public ConsumeFromRabbitImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessage(Update update) {
        mainService.processTextMessage(update);
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
