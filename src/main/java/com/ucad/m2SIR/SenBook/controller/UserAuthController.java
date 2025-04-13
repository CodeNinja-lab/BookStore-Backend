package com.ucad.m2SIR.SenBook.controller;

import com.ucad.m2SIR.SenBook.customTypes.TokenType;
import com.ucad.m2SIR.SenBook.dto.UserLoginDTO;
import com.ucad.m2SIR.SenBook.dto.UserRegisterDTO;
import com.ucad.m2SIR.SenBook.configuration.JwtService;
import com.ucad.m2SIR.SenBook.model.Token;
import com.ucad.m2SIR.SenBook.model.Utilisateur;
import com.ucad.m2SIR.SenBook.repository.TokenRepository;
import com.ucad.m2SIR.SenBook.repository.UtilisateurRepository;
import com.ucad.m2SIR.SenBook.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserAuthController {
    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UtilisateurRepository utilisateurRepository;

    public UserAuthController(UserAuthService userAuthService, AuthenticationManager authenticationManager, JwtService jwtService, TokenRepository tokenRepository, UtilisateurRepository utilisateurRepository) {
        this.userAuthService = userAuthService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> connection(@RequestBody UserLoginDTO user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication auth = authenticationManager.authenticate(token);
        boolean status = auth.isAuthenticated();
        Optional<Utilisateur> fullUser = utilisateurRepository.findByNomUtilisateur(user.getUsername());

        if (status && fullUser.isPresent()) {
            String jwtToken = jwtService.generateToken(user.getUsername());
            Token tokenObj = new Token();
            tokenObj.setUtilisateur(fullUser.get());
            tokenObj.setToken(jwtToken);
            tokenObj.setExpired(false);
            tokenObj.setRevoked(false);
            tokenObj.setTokenType(TokenType.BEARER);
            userAuthService.revokeAllUserTokens(fullUser.get());
            tokenRepository.save(tokenObj);
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed : Impossible d'authentifer l'utilisateur donné", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<String> inscription(@RequestBody UserRegisterDTO user) {
        String response = userAuthService.saveUtilisateur(user);
        HttpStatus status = response.startsWith("Success")
                ? HttpStatus.CREATED
                : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(response, status);
    }
}
