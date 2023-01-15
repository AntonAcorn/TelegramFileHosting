package ru.acorn.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.service.ProduceFromNode;

@Component
public class NodeMessageUtils {
    private final ProduceFromNode produceFromNode;

    public NodeMessageUtils(ProduceFromNode produceFromNode) {
        this.produceFromNode = produceFromNode;
    }

    public void sendAnswer(Update update, String text){
        var message = update.getMessage();
        var chatId = message.getChatId();

        var response = new SendMessage();
        response.setText(text);
        response.setChatId(chatId);
        produceFromNode.sendAnswer(response);
    }
}
