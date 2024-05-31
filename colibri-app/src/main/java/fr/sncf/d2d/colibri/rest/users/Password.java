package fr.sncf.d2d.colibri.rest.users;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@NotNull(message = "Password cannot be omitted.")
@Pattern(regexp = ".{7,}", message = "Password should be at least 7-character long.")
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface Password {

    String message() default "Password cannot contain SNCF in it.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
