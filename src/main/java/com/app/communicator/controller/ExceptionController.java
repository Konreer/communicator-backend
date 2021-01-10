package com.app.communicator.controller;

import com.app.communicator.dto.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@CrossOrigin(origins = "http://localhost:4200")
public class ExceptionController {


    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleAppSecurityException(SecurityException e) {
        return new AppError(e.getMessage(), 401);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AppError handleAccessDeniedException(AccessDeniedException ex){
        return new AppError(ex.getMessage(), 403);
    }
}
