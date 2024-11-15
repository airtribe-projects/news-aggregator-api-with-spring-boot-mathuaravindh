package org.airtribe.newsaggregator.controller;

import org.airtribe.newsaggregator.dto.LoginRequestDTO;
import org.airtribe.newsaggregator.dto.LoginResponseDTO;
import org.airtribe.newsaggregator.entity.User;
import org.airtribe.newsaggregator.service.AuthService;
import org.airtribe.newsaggregator.utility.JWTUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JWTUtility jwtUtility;

    private final AuthService authenticationService;

    public AuthController(JWTUtility jwtUtility, AuthService authenticationService) {
        this.jwtUtility = jwtUtility;
        this.authenticationService = authenticationService;
    }

    @PostMapping("api/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequestDTO loginUserDto){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtUtility.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtUtility.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
