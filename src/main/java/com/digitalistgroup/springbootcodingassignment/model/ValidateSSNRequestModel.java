package com.digitalistgroup.springbootcodingassignment.model;

import com.digitalistgroup.springbootcodingassignment.annotation.ValidSSN;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidSSN
public class ValidateSSNRequestModel {
    @NotNull(message = "{ssn.not.empty}")
    @Size(min = 11, max = 11, message = "{ssh.invalid.length}")
    private String ssn;

    @NotNull(message = "{country.code.not.empty}")
    @Pattern(regexp = "^FI$", message = "{country.code.not.supported}")
    private String countryCode;
}
