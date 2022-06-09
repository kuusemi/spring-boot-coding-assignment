package com.digitalistgroup.springbootcodingassignment.validation;

import com.digitalistgroup.springbootcodingassignment.model.ValidateSSNResponseModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SSNValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidateSSNResponseModel> handleException(MethodArgumentNotValidException e) {
        log.error("Validation exception occurred: {}", e.getLocalizedMessage());

        return ResponseEntity.ok().body(
                new ValidateSSNResponseModel(false)
        );

    }

}
