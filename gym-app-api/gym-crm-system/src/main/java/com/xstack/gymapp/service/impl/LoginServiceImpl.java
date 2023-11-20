package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.enumeration.UserType;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.LoginResponse;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import com.xstack.gymapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TrainerService trainerService;

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );

        UserDto user = userService.getUserByUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user.getId());
        tokenService.saveUserToken(user, jwtToken);
        UserType userType = trainerService.isTrainerByUserDto(user) ? UserType.TRAINER : UserType.TRAINEE;

        return LoginResponse.builder()
                .isLoggedIn(true)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userType(userType.getName())
                .build();
    }

    public boolean changePassword(PasswordChangeRequest request) {
        UserDto userDto = userService.getUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getOldPassword(), userDto.getPassword())) {
            return false;
        }

        userDto.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.createUser(userDto);
        return true;
    }

    @Override
    public void logout(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        if (tokenService.updateTokenAfterLogout(jwt)) {
            SecurityContextHolder.clearContext();
        }
    }

    public String getNewAuthTokenIfPasswordChanged(String oldToken, PasswordChangeRequest request) {
        if (request.getNewPassword() != null) {
            UserDto userDto = userService.getUserByUsername(request.getUsername());

            return jwtService.generateToken(userDto);
        }

        return oldToken;
    }

}
