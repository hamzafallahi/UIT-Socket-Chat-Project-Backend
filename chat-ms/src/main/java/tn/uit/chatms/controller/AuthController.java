package tn.uit.chatms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.uit.chatms.dto.AuthLoginRequest;
import tn.uit.chatms.dto.AuthLoginResponse;
import tn.uit.chatms.dto.AuthRegisterRequest;
import tn.uit.chatms.service.AuthService;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthLoginResponse> register(@RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
