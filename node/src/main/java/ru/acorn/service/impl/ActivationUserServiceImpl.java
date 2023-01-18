package ru.acorn.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.acorn.entity.AppUser;
import ru.acorn.entity.MailParams;
import ru.acorn.repository.AppUserRepository;
import ru.acorn.service.ActivationUserService;
import ru.acorn.utils.CryptoTool;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static ru.acorn.entity.enums.UserState.BASIC_STATE;
import static ru.acorn.entity.enums.UserState.WAITING_FOR_REGISTRATION;

@Service
@Log4j
public class ActivationUserServiceImpl implements ActivationUserService {
    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;

    //@Value("${service.mail.uri}")
    private String mailServiceUri = "http://127.0.0.1:8083/mail/send";

    public ActivationUserServiceImpl(AppUserRepository appUserRepository, CryptoTool cryptoTool) {
        this.appUserRepository = appUserRepository;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.isActive()) {
            return "You are already registered";
        } else if (appUser.getEmail() != null) {
            return "Check your mail";
        }
        appUser.setUserState(WAITING_FOR_REGISTRATION);
        appUserRepository.save(appUser);
        return "Please, enter your email";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try{
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();

        } catch (AddressException e) {
            return "Enter valid email";
        }
        var optional = appUserRepository.findAppUserByEmail(email);
        if(optional.isEmpty()){
            appUser.setEmail(email);
            appUser.setUserState(BASIC_STATE);
            appUserRepository.save(appUser);

            var cryptoUserId = cryptoTool.hashFromId(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);
            if(response.getStatusCode() != HttpStatus.OK){
                var msg = String.format("Sending mail to %s is unsuccessful", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserRepository.save(appUser);
                return msg;
            }
            return "Mail to you was send. Check it and activate your account";
        }else{
            return "This email is used. Enter valid email or use /cancel";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        var request = new HttpEntity<MailParams>(mailParams, headers);
        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                request,
                String.class);
    }
}
