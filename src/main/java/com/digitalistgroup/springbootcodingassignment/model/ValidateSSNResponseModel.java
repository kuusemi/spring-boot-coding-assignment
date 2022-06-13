package com.digitalistgroup.springbootcodingassignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ValidateSSNResponseModel {
    @NotNull
    @JsonProperty("ssn_valid")
    private boolean ssnValid;
}
