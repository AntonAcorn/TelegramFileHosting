package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acorn.entity.AppUser;
import ru.acorn.service.AppUserService;
import ru.acorn.service.MainService;
import ru.acorn.service.ProduceFromNode;
import ru.acorn.service.RawDataService;
import ru.acorn.utils.NodeMessageUtils;

@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataService rawDataService;
    private final NodeMessageUtils nodeMessageUtils;
    private final ProduceFromNode produceFromNode;
    private  final AppUserService appUserService;

    public MainServiceImpl(RawDataService rawDataService,
                           NodeMessageUtils nodeMessageUtils,
                           ProduceFromNode produceFromNode,
                           AppUserService appUserService) {
        this.rawDataService = rawDataService;
        this.nodeMessageUtils = nodeMessageUtils;
        this.produceFromNode = produceFromNode;
        this.appUserService = appUserService;
    }

    @Override
    public void processTextMessage(Update update) {
        rawDataService.save(update);
        var AppUser = appUserService.findOrSaveAppUser(update);



        log.debug(update.getMessage().getText());
        var msg = nodeMessageUtils.generateAnswer(update, "Hello from Node");
        produceFromNode.sendAnswer(msg);
    }
}
