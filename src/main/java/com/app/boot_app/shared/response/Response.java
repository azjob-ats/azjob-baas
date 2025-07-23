package com.app.boot_app.shared.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class Response {

    public static ApiResponse<?> error(String message, HttpStatus status, ApiResponse.Error error) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .statusCode(status.value())
                .error(error)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(HttpStatus.CREATED.value())
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    public static ApiResponse<Void> noContent() {
        return ApiResponse.<Void>builder()
                .success(true)
                .message("No content")
                .statusCode(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
