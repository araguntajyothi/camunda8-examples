package com.camunda.wrapper.validators;



import com.camunda.wrapper.annotations.NotNull;
import com.camunda.wrapper.utils.IsObjectEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullValidator implements ConstraintValidator<NotNull, IsObjectEmpty> {

    @Override
    public boolean isValid(IsObjectEmpty value, ConstraintValidatorContext context) {
        return !value.isEmpty();
    }

}

