package com.myweddi.modules.registration;

import com.myweddi.users.authentication.Auth;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;

public class RegistrationValidate implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
