package com.digitalistgroup.springbootcodingassignment.validation;

import com.digitalistgroup.springbootcodingassignment.annotation.ValidSSN;
import com.digitalistgroup.springbootcodingassignment.model.ValidateSSNRequestModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SSNConstraintValidator implements ConstraintValidator<ValidSSN, ValidateSSNRequestModel> {
    private final String[] validSeparators = new String[] {
            "+", "-", "A"
    };

    private static final int DIVIDER = 31;

    private final Map<Integer, String> validControlCharacters = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(0, "0"),
            new AbstractMap.SimpleEntry<>(1, "1"),
            new AbstractMap.SimpleEntry<>(2, "2"),
            new AbstractMap.SimpleEntry<>(3, "3"),
            new AbstractMap.SimpleEntry<>(4, "4"),
            new AbstractMap.SimpleEntry<>(5, "5"),
            new AbstractMap.SimpleEntry<>(6, "6"),
            new AbstractMap.SimpleEntry<>(7, "7"),
            new AbstractMap.SimpleEntry<>(8, "8"),
            new AbstractMap.SimpleEntry<>(9, "9"),
            new AbstractMap.SimpleEntry<>(10, "A"),
            new AbstractMap.SimpleEntry<>(11, "B"),
            new AbstractMap.SimpleEntry<>(12, "C"),
            new AbstractMap.SimpleEntry<>(13, "D"),
            new AbstractMap.SimpleEntry<>(14, "E"),
            new AbstractMap.SimpleEntry<>(15, "F"),
            new AbstractMap.SimpleEntry<>(16, "H"),
            new AbstractMap.SimpleEntry<>(17, "J"),
            new AbstractMap.SimpleEntry<>(18, "K"),
            new AbstractMap.SimpleEntry<>(19, "L"),
            new AbstractMap.SimpleEntry<>(20, "M"),
            new AbstractMap.SimpleEntry<>(21, "N"),
            new AbstractMap.SimpleEntry<>(22, "P"),
            new AbstractMap.SimpleEntry<>(23, "R"),
            new AbstractMap.SimpleEntry<>(24, "S"),
            new AbstractMap.SimpleEntry<>(25, "T"),
            new AbstractMap.SimpleEntry<>(26, "U"),
            new AbstractMap.SimpleEntry<>(27, "V"),
            new AbstractMap.SimpleEntry<>(28, "W"),
            new AbstractMap.SimpleEntry<>(29, "X"),
            new AbstractMap.SimpleEntry<>(30, "Y")
    );

    @Override
    public boolean isValid(ValidateSSNRequestModel value, ConstraintValidatorContext context) {
        if (value.getSsn() != null && "".equals(value.getSsn())) {
            return false;
        }
        if (value.getSsn() != null && !checkSeparator(value.getSsn())) {
            return false;
        }
        if (value.getSsn() != null && !checkControlCharacter(value.getSsn())) {
            return false;
        }
        return value.getSsn() == null || checkControlCharacter(value.getSsn());
    }

    private boolean checkSeparator(String ssn) {
        String separator = String.valueOf(ssn.charAt(6));

        return Arrays.asList(validSeparators).contains(separator);
    }

    private boolean checkControlCharacter(String ssn) {
        String controlCharacter = String.valueOf(ssn.charAt(ssn.length() - 1));
        String ssnNumbers = extractNumbers(ssn.substring(0, ssn.length() - 1));
        if (ssnNumbers.length() != 9) {
            return false;
        }
        int key = calculateKey(ssnNumbers);

        return controlCharacter.equals(validControlCharacters.get(key));
    }

    private int calculateKey(String ssnNumbers) {
        BigDecimal ssnBigDecimal = new BigDecimal(Long.parseLong(ssnNumbers))
                .divide(new BigDecimal(DIVIDER), MathContext.DECIMAL128);

        return new BigDecimal(
                ssnBigDecimal.doubleValue() - Math.floor(ssnBigDecimal.doubleValue()),
                MathContext.DECIMAL128
        ).multiply(new BigDecimal(DIVIDER, new MathContext(0, RoundingMode.HALF_UP))).intValue();
    }

    private String extractNumbers(String ssn) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(ssn);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        return builder.toString();
    }
}
