package ru.acorn.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {
    public SendMessage generateMessage (Update update, String text){
        var message = update.getMessage();
        var chatId = message.getChatId();

        var response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        return response;
    }

}
