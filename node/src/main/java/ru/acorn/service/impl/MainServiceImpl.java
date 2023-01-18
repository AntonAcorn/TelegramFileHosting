package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.entity.AppDocument;
import ru.acorn.entity.AppPhoto;
import ru.acorn.entity.AppUser;
import ru.acorn.repository.AppUserRepository;
import ru.acorn.service.*;
import ru.acorn.utils.LinkValue;
import ru.acorn.utils.NodeMessageUtils;

import static ru.acorn.entity.enums.UserState.BASIC_STATE;
import static ru.acorn.entity.enums.UserState.WAITING_FOR_REGISTRATION;
import static ru.acorn.service.command.ServiceCommand.*;

@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataService rawDataService;
    private final NodeMessageUtils nodeMessageUtils;
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    private final ActivationUserService activationUserService;

    public MainServiceImpl(RawDataService rawDataService,
                           NodeMessageUtils nodeMessageUtils,
                           AppUserService appUserService,
                           AppUserRepository appUserRepository,
                           FileService fileService,
                           ActivationUserService activationUserService) {
        this.rawDataService = rawDataService;
        this.nodeMessageUtils = nodeMessageUtils;
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
        this.fileService = fileService;
        this.activationUserService = activationUserService;
    }

    @Override
    public void processTextMessage(Update update) {
        rawDataService.save(update);
        var appUser = appUserService.findOrSaveAppUser(update);
        var commandFromUser = update.getMessage().getText();
        var response = "";

        if (CANCEL.equalCommand(commandFromUser)) {
            response = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(appUser.getUserState())) {
            response = commandProcess(appUser, commandFromUser);
        } else if (WAITING_FOR_REGISTRATION.equals(appUser.getUserState())) {
            response = activationUserService.setEmail(appUser, commandFromUser);
        } else {
            log.error(response);
            response = "Unknown error, try to use /cancel";
        }

        nodeMessageUtils.sendAnswer(update, response);
    }

    @Override
    public void processDocMessage(Update update) {
        rawDataService.save(update);
        var appUser = appUserService.findOrSaveAppUser(update);

        if (isNotAllowedToDownload(appUser, update)) {
            return;
        }
        try {
            AppDocument appDocument = fileService.processDoc(update.getMessage());
            var link = fileService.generateLink(appDocument.getId(), LinkValue.DOC_VALUE);
            var response = "The document has been successfully uploaded, here is the link for downloading it:\n" + link;
            nodeMessageUtils.sendAnswer(update, response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Downloading unsuccessful");
            //TODO
        }

    }

    @Override
    public void processPhotoMessage(Update update) {
        rawDataService.save(update);
        var appUser = appUserService.findOrSaveAppUser(update);

        if (isNotAllowedToDownload(appUser, update)) {
            return;
        }

        try {
            AppPhoto appPhoto = fileService.processPhoto(update.getMessage());
            var link = fileService.generateLink(appPhoto.getId(), LinkValue.PHOTO_VALUE);
            var response = "The photo has been successfully uploaded, here is the link for downloading it:\n " + link;
            nodeMessageUtils.sendAnswer(update, response);
        }catch (RuntimeException e){
            throw new RuntimeException("Downloading unsuccessful");
            //TODO
        }


    }

    private boolean isNotAllowedToDownload(AppUser appUser, Update update) {
        var state = appUser.getUserState();
        if (!appUser.isActive()) {
            var error = "Please, register or activate your account for downloading content. Write /help or /cancel";
            nodeMessageUtils.sendAnswer(update, error);
            return true;
        } else if (!appUser.getUserState().equals(BASIC_STATE)) {
            var error = "Please, cancel the current command by using the command /cancel";
            nodeMessageUtils.sendAnswer(update, error);
        }
        return false;
    }

    private String commandProcess(AppUser appUser, String commandFromUser) {
        if (REGISTRATION.equalCommand(commandFromUser)) {
            return activationUserService.registerUser(appUser);
        } else if (HELP.equalCommand(commandFromUser)) {
            return "List of available commands: \n" +
                    "/cancel - canceling the execution of the current command \n" +
                    "/registration - registration of user";
        } else if (START.equalCommand(commandFromUser)) {
            return "Greetings. Kindly upload a picture or document, and I shall provide you with a link for downloading it";
        } else {
            return "Unknown command. Use /help or /start";
        }
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setUserState(BASIC_STATE);
        appUserRepository.save(appUser);
        return "Command is canceled. Use /start";
    }
}
