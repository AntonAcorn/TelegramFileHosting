package ru.acorn.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acorn.entity.AppUser;

import java.util.Optional;

public interface AppUserService {
    AppUser findOrSaveAppUser(Update update);
}
