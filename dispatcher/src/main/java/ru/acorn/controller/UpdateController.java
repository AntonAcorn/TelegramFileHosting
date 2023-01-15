package ru.acorn.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.utils.MessageUtils;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;

    public UpdateController(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
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
            log.error("Received update is null");
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

    public void setView(SendMessage response) {
        telegramBot.sendAnswerMessage(response);
    }

    private void processPhoto(Update update) {


    }

    private void processDoc(Update update) {

    }

    private void processTextMessage(Update update) {

    }

}
