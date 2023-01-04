package com.example.springsecurityjwt.common.advice;

import com.example.springsecurityjwt.common.exception.BadRequestException;
import com.example.springsecurityjwt.common.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdviceController {

    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<ErrorResponse> unAuthenticatedException(BadRequestException e){
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
