package com.app.communicator.security.tokens;

import com.app.communicator.dto.securityDto.RefreshTokenDto;
import com.app.communicator.dto.securityDto.TokensDto;
import com.app.communicator.entity.User;
import com.app.communicator.repository.UserRepository;
import com.app.communicator.security.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokensService {

    @Value("${tokens.access.expiration-time-ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${tokens.refresh.expiration-time-ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${tokens.refresh.property}")
    private String refreshTokenProperty;

    @Value("${tokens.prefix}")
    private String tokenPrefix;

    private final UserRepository userRepository;
    private final SecretKey secretKey;


    public TokensDto generateTokens(Authentication authentication) {
        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new SecurityException("Cannot find user with username " + authentication.getName()));
        String userId = String.valueOf(user.getId());

        long accessTokenExpirationDateMs = System.currentTimeMillis() + accessTokenExpirationTimeMs;
        Date accessTokenExpirationDate = new Date(accessTokenExpirationDateMs);

        long refreshTokenExpirationDateMs = System.currentTimeMillis() + refreshTokenExpirationTimeMs;
        Date refreshTokenExpirationDate = new Date(refreshTokenExpirationDateMs);

        String accessToken = Jwts
                .builder()
                .setSubject(userId)
                .setExpiration(accessTokenExpirationDate)
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts
                .builder()
                .setSubject(userId)
                .setExpiration(refreshTokenExpirationDate)
                .claim(refreshTokenProperty, accessTokenExpirationDateMs)
                .signWith(secretKey)
                .compact();

        return TokensDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UsernamePasswordAuthenticationToken parseToken(String token) {
        if (Objects.isNull(token)) {
            throw new SecurityException("token is null");
        }

        if (!token.startsWith(tokenPrefix)) {
            throw new SecurityException("token is not correct");
        }

        var accessToken = token.replace(tokenPrefix, "");
        if (!isTokenValid(accessToken)) {
            throw new SecurityException("access token is not valid");
        }

        var id = getId(accessToken);
        var userFromDb = userRepository
                .findById(id)
                .orElseThrow(() -> new SecurityException("cannot find user from db"));

        return new UsernamePasswordAuthenticationToken(
                new CustomUser(
                        userFromDb.getId(),
                        userFromDb.getUsername(),
                        userFromDb.getPassword(),
                        userFromDb.getIsEnabled(), true, true, true,
                        Collections.emptyList()
                ),
                null,
                null);
    }

    public TokensDto parseTokenFromRefreshToken(RefreshTokenDto refreshTokenDto) {
        String token = refreshTokenDto.getToken();

        if (Objects.isNull(token)) {
            throw new SecurityException("Token is null");
        }

        if (!isTokenValid((token))) {
            throw new SecurityException("Token is not valid");
        }

        long accessTokenExpirationDateMs = Long.parseLong(getClaims(token).get(refreshTokenProperty).toString());

        Long userId = getId(token);
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new SecurityException("Cannot find user with id " + userId));

        String userIdAsString = String.valueOf(user.getId());
        Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + accessTokenExpirationTimeMs);
        Date refreshTokenExpirationDate = getExpiration(token);

        String accessToken = Jwts
                .builder()
                .setSubject(userIdAsString)
                .setExpiration(accessTokenExpirationDate)
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts
                .builder()
                .setSubject(userIdAsString)
                .setExpiration(refreshTokenExpirationDate)
                .claim(refreshTokenProperty, accessTokenExpirationDateMs)
                .signWith(secretKey)
                .compact();

        return TokensDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Long getId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    private Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    private boolean isTokenValid(String token) {
        return getExpiration(token).after(new Date());
    }
}
