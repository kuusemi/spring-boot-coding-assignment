package com.digitalistgroup.springbootcodingassignment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateSSNRequestModel {
    @NotNull(message = "Social Security Number can't be empty")
    @Size(min = 11, max = 11, message = "Social Security Number should be exactly 11 characters long")
    private String ssn;

    @NotNull(message = "Country code can't be empty")
    @Pattern(regexp = "^FI$", message = "Only supported country codes currently are: 'FI'")
    private String countryCode;
}
