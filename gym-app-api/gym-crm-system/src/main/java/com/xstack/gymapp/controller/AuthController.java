package com.xstack.gymapp.controller;

import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import com.xstack.gymapp.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController extends BaseController {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ACCESS_CONTROL = "Access-Control-Expose-Headers";

    private LoginService loginService;

    @Operation(summary = "Login user", description = "Login endpoint")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.login(loginRequest));
    }

    @Operation(summary = "Change password", description = "Change user password")
    @PutMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody PasswordChangeRequest request,
                               HttpServletResponse httpResponse, HttpServletRequest httpRequest) {

        if (loginService.changePassword(request)) {
            String oldAuthToken = httpRequest.getHeader(TOKEN_HEADER);
            String authToken = loginService.getNewAuthTokenIfPasswordChanged(
                    httpRequest.getHeader(oldAuthToken), request);
            httpResponse.setHeader(ACCESS_CONTROL, TOKEN_HEADER);
            httpResponse.setHeader(TOKEN_HEADER, TOKEN_PREFIX + authToken);
        }
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        loginService.logout(request, response, authentication);
    }

}