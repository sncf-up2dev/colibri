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
@NotNull
@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_]*")
@Constraint(validatedBy = {})
public @interface Username {

    String message() default "Not a valid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
