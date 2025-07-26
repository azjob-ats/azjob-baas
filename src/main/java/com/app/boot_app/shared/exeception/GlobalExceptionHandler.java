package com.app.boot_app.shared.exeception;

import com.app.boot_app.shared.exeception.model.BadRequestException;
import com.app.boot_app.shared.exeception.model.ConflictException;
import com.app.boot_app.shared.exeception.model.InternalServerErrorException;
import com.app.boot_app.shared.exeception.model.NotFoundException;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleConflict(ConflictException ex) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(InternalServerErrorException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(MethodArgumentNotValidException  ex) {

            List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + getFriendlyMessage(error))
            .collect(Collectors.toList());
 
            var error = ApiResponse.Error.builder()
                .code("Validation/FieldError")
                .message(errors.toString())
                .build();

        var response = Response.error(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                error
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponse(BaseHttpException ex, HttpStatus status) {
        var error = ApiResponse.Error.builder()
                .code(ex.getCode())
                .message("An unexpected error occurred. Please try again later.")
                .build();

        var response = Response.error(
                ex.getFriendlyMessage(),
                status,
                error
        );

        return new ResponseEntity<>(response, status);
    }

    private String getFriendlyMessage(FieldError error) {
       return error.getDefaultMessage();
    }
}
