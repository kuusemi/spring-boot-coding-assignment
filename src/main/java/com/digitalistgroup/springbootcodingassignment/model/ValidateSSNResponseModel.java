package com.digitalistgroup.springbootcodingassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ValidateSSNResponseModel {
    @NotNull
    private boolean ssnValid;
}
