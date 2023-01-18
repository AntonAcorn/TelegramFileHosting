package ru.acorn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.acorn.service.UserActivationService;

@RestController
@RequestMapping("/user")
public class ActivationController {
    private final UserActivationService activationService;

    public ActivationController(UserActivationService activationService) {
        this.activationService = activationService;
    }

    @GetMapping("/activation")
    public ResponseEntity<?> activate (@RequestParam ("id") String id){
        var res = activationService.activation(id);
        if(res){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }

    }
}
