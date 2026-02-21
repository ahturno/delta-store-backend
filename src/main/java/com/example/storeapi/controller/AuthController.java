package com.example.storeapi.controller;

import com.example.storeapi.dto.*;
import com.example.storeapi.entity.User;
import com.example.storeapi.exception.auth.*;
import com.example.storeapi.security.JwtUtil;
import com.example.storeapi.security.PasswordEncoder;
import com.example.storeapi.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping({"/", ""})
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // 1️⃣ Get cookies from request
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, AuthErrorResponse.builder().message("No authentication cookie found").build());
        }

        // 2️⃣ Find the access_token cookie
        Optional<Cookie> tokenCookie =
                java.util.Arrays.stream(cookies)
                        .filter(c -> "access_token".equals(c.getName()))
                        .findFirst();

        if (tokenCookie.isEmpty()) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, AuthErrorResponse.builder().message("No access token cookie found").build());
        }

        String token = tokenCookie.get().getValue();

        // 3️⃣ Verify and parse JWT
        try {
            JwtUtil.JwtPayload payload = JwtUtil.verifyToken(token);
            User user = userService.getById(payload.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            // 4️⃣ Fetch user if needed
            return ResponseEntity.ok(new AuthResponse(user.getId(), user.getEmail(), user.getFullName()));
        } catch (Exception e) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, AuthErrorResponse.builder().message("Invalid or expired token").build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            RegisterErrorResponse.RegisterErrorResponseBuilder builder = RegisterErrorResponse.builder();

            bindingResult.getFieldErrors().forEach(fe -> {
                switch (fe.getField()) {
                    case "email" -> builder.email(fe.getDefaultMessage());
                    case "password" -> builder.password(fe.getDefaultMessage());
                }
            });

            throw new RegisterException(
                    HttpStatus.BAD_REQUEST,
                    builder.build()
            );
        }

        if (userService.checkByEmail(request.getEmail())) {
            throw new RegisterException(HttpStatus.CONFLICT, RegisterErrorResponse.builder().email("Email already registered").build());
        }
        User user = userService.add(request.getEmail(), request.getFullName(), request.getPassword());

        // Generate JWT
        String token = JwtUtil.generateToken(user.getId(), user.getEmail());

        RegisterResponse registerResponse = new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                token
        );

        // ✅ Create HttpOnly cookie
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true); // JS cannot access this cookie
        cookie.setSecure(true);   // only send over HTTPS (set false if local dev)
        cookie.setPath("/");      // available to entire domain
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30 day in seconds
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        User user = userService.getByEmail(request.getEmail()).orElseThrow(() -> new LoginException(HttpStatus.UNAUTHORIZED, LoginErrorResponse.builder().defaultError("Email or password is incorrect").build()));

        if (!PasswordEncoder.verify(request.getPassword(), user.getPasswordHash())) {
            throw new LoginException(HttpStatus.UNAUTHORIZED, LoginErrorResponse.builder().defaultError("Email or password is incorrect").build());
        }
        String token = JwtUtil.generateToken(user.getId(), user.getEmail());

        // ✅ Create HttpOnly cookie
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true); // JS cannot access this cookie
        cookie.setSecure(true);   // only send over HTTPS (set false if local dev)
        cookie.setPath("/");      // available to entire domain
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30 day in seconds
        response.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName()
        ));
    }
}