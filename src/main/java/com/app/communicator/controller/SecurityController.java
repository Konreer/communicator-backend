package com.app.communicator.controller;

import com.app.communicator.dto.securityDto.RefreshTokenDto;
import com.app.communicator.dto.securityDto.TokensDto;
import com.app.communicator.security.tokens.TokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SecurityController {

    private final TokensService tokensService;

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokensDto refreshTokens(@RequestBody RefreshTokenDto refreshTokenDto) {
        return tokensService.parseTokenFromRefreshToken(refreshTokenDto);
    }

}
