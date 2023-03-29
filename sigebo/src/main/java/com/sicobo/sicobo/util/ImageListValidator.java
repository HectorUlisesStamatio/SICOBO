package com.sicobo.sicobo.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageListValidator implements ConstraintValidator<ImageList, List<MultipartFile>> {
    private static final String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png"};

    @Override
    public void initialize(ImageList constraintAnnotation) {}

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        for (MultipartFile image : images) {
            if (!Arrays.asList(ALLOWED_FILE_TYPES).contains(image.getContentType())) {
                return false;
            }
        }
        return true;
    }
}
