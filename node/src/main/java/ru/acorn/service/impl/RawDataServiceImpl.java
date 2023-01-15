package ru.acorn.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.entity.RawData;
import ru.acorn.repository.RawDataRepository;
import ru.acorn.service.RawDataService;

@Service
public class RawDataServiceImpl implements RawDataService {
    private final RawDataRepository rawDataRepository;

    public RawDataServiceImpl(RawDataRepository rawDataRepository) {
        this.rawDataRepository = rawDataRepository;
    }

    @Override
    public void save(Update update) {
        RawData rawDataToSave = RawData.builder()
                .event(update)
                .build();
        rawDataRepository.save(rawDataToSave);
    }
}
