package ru.acorn.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.entity.RawData;

public interface RawDataService {
    void save(Update update);
}
