package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acorn.entity.AppUser;
import ru.acorn.entity.enums.UserState;
import ru.acorn.repository.AppUserRepository;
import ru.acorn.service.AppUserService;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service
@Log4j
public class AppUserServiceImpl implements AppUserService {
    public final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();

        var persistentAppUser = appUserRepository.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistentAppUser.isEmpty()){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstname(telegramUser.getFirstName())
                    .lastname(telegramUser.getLastName())
                    .isActive(false)
                    .userState(UserState.BASIC_STATE)
                    .build();
            return appUserRepository.save(transientAppUser);
        }
        return persistentAppUser.orElseThrow(NotFoundException::new);
        //TODO EXCEPTION
    }
}
