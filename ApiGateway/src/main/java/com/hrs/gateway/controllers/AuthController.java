package com.hrs.gateway.controllers;

import com.hrs.gateway.payload.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login
            (
                    @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
                    @AuthenticationPrincipal OidcUser user,
                    Model model
            ) {
        logger.info("user email id: {} ", user.getEmail());
        AuthResponse auth = new AuthResponse();
        /*setting email to auth-response*/
        auth.setUserId(user.getEmail());
        /*setting token to auth-response*/
        auth.setAccessToken(client.getAccessToken().getTokenValue());
        /*setting token refresh*/
        auth.setRefreshToken(client.getRefreshToken().getTokenValue());
        /*setting expiry time*/
        auth.setExpiresAt(client.getAccessToken().getExpiresAt().getEpochSecond());
        /*setting authorities*/
        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        auth.setAuthorities(authorities);
        return new ResponseEntity<>(auth, HttpStatus.OK);
    }
}
