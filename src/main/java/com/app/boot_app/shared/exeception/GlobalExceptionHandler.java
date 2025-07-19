package com.app.boot_app.shared.exeception;


import com.app.boot_app.domain.auth.exception.*;
import com.app.boot_app.shared.response.ApiResponse;
import com.app.boot_app.shared.response.Response;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        var error = ApiResponse.Error.builder()
                .code("unexpected-error")
                .message(messageSource.getMessage("error.unexpected", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale()))
                .build();

        var response = Response.error(
                messageSource.getMessage("error.unexpected.generic", null, LocaleContextHolder.getLocale()),
                HttpStatus.INTERNAL_SERVER_ERROR,
                error
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponse(BaseHttpException ex, HttpStatus status) {
        var error = ApiResponse.Error.builder()
                .code(ex.getCode())
                .message(ex.getFriendlyMessage())
                .build();

        var response = Response.error(
                ex.getFriendlyMessage(),
                status,
                error
        );

        return new ResponseEntity<>(response, status);
    }
}
