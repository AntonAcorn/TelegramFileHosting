package ru.acorn.service;

import ru.acorn.entity.AppUser;

public interface ActivationUserService {
    String registerUser(AppUser appUser);
    String setEmail (AppUser appUser, String email);
}
