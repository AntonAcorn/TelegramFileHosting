package ru.acorn.service.impl;

import org.springframework.stereotype.Service;
import ru.acorn.repository.AppUserRepository;
import ru.acorn.service.UserActivationService;
import ru.acorn.utils.CryptoTool;

@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;

    public UserActivationServiceImpl(AppUserRepository appUserRepository, CryptoTool cryptoTool) {
        this.appUserRepository = appUserRepository;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idFromHash(cryptoUserId);
        var optional = appUserRepository.findById(userId);
        if(optional.isPresent()){
            var user = optional.get();
            user.setActive(true);
            appUserRepository.save(user);
            return true;
        }
        return false;
    }
}
