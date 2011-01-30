package org.jboss.lectures.auction.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CityValidator implements ConstraintValidator<City, String>{

	@Override
	public void initialize(City constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
		{
			return true;
		}
		if (value.equals("Brno") || value.equals("Praha"))
		{
			return true;
		}
		return false;
	}
}
