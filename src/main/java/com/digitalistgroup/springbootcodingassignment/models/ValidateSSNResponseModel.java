package com.digitalistgroup.springbootcodingassignment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ValidateSSNResponseModel {
    @NotNull
    private boolean ssnValid;
}
