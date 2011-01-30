package org.jboss.lectures.auction.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy={CityValidator.class})
public @interface City {
	
	String message() default "Unknown city";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
