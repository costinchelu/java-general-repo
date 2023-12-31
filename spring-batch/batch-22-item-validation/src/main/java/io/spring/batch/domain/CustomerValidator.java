package io.spring.batch.domain;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;


public class CustomerValidator implements Validator<Customer> {

	@Override
	public void validate(Customer value) throws ValidationException {
		if(value.firstName().startsWith("A")) {
			throw new ValidationException("First names that begin with A are invalid: " + value);
		}
	}
}
