package com.digitalistgroup.springbootcodingassignment.controller;

import com.digitalistgroup.springbootcodingassignment.model.ValidateSSNRequestModel;

import com.digitalistgroup.springbootcodingassignment.model.ValidateSSNResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/validate_ssn")
public class SSNValidationController {

    @PostMapping(
            consumes = {APPLICATION_JSON_VALUE},
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ValidateSSNResponseModel> validateSSN(@Valid @RequestBody ValidateSSNRequestModel ssnDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(new ValidateSSNResponseModel(true));
    }
}
