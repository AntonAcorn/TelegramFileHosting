package ru.acorn.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.service.ProduceToRabbit;
import ru.acorn.utils.MessageUtils;

import static ru.acorn.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateProcessor {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final ProduceToRabbit produceToRabbit;


    public UpdateProcessor(MessageUtils messageUtils, ProduceToRabbit produceToRabbit) {
        this.messageUtils = messageUtils;
        this.produceToRabbit = produceToRabbit;
    }


    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }
        if (update.getMessage() != null) {
            distributeMessageByType(update);
        } else {
            log.error("Received update is null " + update);
        }

    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDoc(update);
        } else if (message.hasPhoto()) {
            processPhoto(update);
        } else {
            setUnsupportedMessage(update);
            log.error("Unsupported media type is provided");
        }
    }

    private void setUnsupportedMessage(Update update) {
        var response = messageUtils.generateMessage(update, "Unsupported media type is provided");
        setView(response);
    }

    private void setReceivedMessage(Update update) {
        var message = messageUtils.generateMessage(update, "File is in process");
        setView(message);
    }

    public void setView(SendMessage response) {
        telegramBot.sendAnswerMessage(response);
    }

    private void processPhoto(Update update) {
        produceToRabbit.produce(PHOTO_MESSAGE_UPDATE, update);
        setReceivedMessage(update);
    }

    private void processDoc(Update update) {
        produceToRabbit.produce(DOC_MESSAGE_UPDATE, update);
        setReceivedMessage(update);
    }

    private void processTextMessage(Update update) {
        produceToRabbit.produce(TEXT_MESSAGE_UPDATE, update);
    }

}
