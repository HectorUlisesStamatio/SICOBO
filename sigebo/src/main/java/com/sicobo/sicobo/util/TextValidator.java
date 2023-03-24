package com.sicobo.sicobo.util;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextValidator {
    private static final String TEXT_PATTERN = "^(?!\\s)(?!.*\\s$)[a-zA-ZñÑáéíóúüÁÉÍÓÚÜ0-9&%#\\s]+$";
    private static final Pattern pattern = Pattern.compile(TEXT_PATTERN);

    public boolean isValid(final String rfc) {
        Matcher matcher = pattern.matcher(rfc);
        return matcher.matches();
    }

}
