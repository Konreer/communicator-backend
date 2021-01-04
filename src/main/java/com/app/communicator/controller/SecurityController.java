package com.app.communicator.controller;

import com.app.communicator.dto.securityDto.RefreshTokenDto;
import com.app.communicator.dto.securityDto.RegisterUserDto;
import com.app.communicator.dto.securityDto.TokensDto;
import com.app.communicator.exception.FormDataException;
import com.app.communicator.security.tokens.TokensService;
import com.app.communicator.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SecurityController {

    private final TokensService tokensService;
    private final SecurityService securityService;

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokensDto refreshTokens(@RequestBody RefreshTokenDto refreshTokenDto) {
        return tokensService.parseTokenFromRefreshToken(refreshTokenDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long register(@RequestBody @Valid RegisterUserDto registerUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){ throw new FormDataException("Data in form is invalid", bindingResult); }
        return securityService.register(registerUserDto);
    }

}
