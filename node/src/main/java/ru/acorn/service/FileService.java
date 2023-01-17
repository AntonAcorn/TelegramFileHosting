package ru.acorn.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acorn.entity.AppDocument;
import ru.acorn.entity.AppPhoto;
import ru.acorn.utils.LinkValue;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generateLink(Long id, LinkValue linkValue);
}
