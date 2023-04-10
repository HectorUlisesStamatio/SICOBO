package com.sicobo.sicobo.util;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PhoneValidator {
    private static final String PHONE_PATTERN = "[0-9]{1,15}";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    public boolean isValid(final String phone) {
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}
