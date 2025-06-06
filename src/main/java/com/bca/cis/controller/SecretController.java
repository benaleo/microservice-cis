package com.bca.cis.controller;

import com.bca.cis.service.other.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class SecretController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/generate-token")
    public String generateToken(
            @RequestParam("email") String email,
            @RequestParam(value = "role", required = false, defaultValue = "user") String role) {
        return jwtTokenService.generateToken(email, role);
    }

}
