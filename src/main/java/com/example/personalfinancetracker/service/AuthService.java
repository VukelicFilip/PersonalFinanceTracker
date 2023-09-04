package com.example.personalfinancetracker.service;

import com.example.personalfinancetracker.repository.UserRepository;
import com.example.personalfinancetracker.security.JwtUtils;
import com.example.personalfinancetracker.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public String login(String username, String password) {

        Authentication authentication;

        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return jwt;
    }

    public Long getJwtUserId() {
        UserDetailsImpl tokenUser;
        try {
            tokenUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println("STIGAO DO STAMPE");
            System.out.println(tokenUser.getUserId());
            return tokenUser.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            /*throw new UnauthorizedException("User is not signed in");*/
        }
        //TODO POGLEDATI OVO MALO JE UZAS
        return null;
    }
}
