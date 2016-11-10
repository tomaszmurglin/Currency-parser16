package pl.parser.nbp.validation;

import org.junit.Test;

import pl.parser.nbp.exception.UserInputValidationException;

import static org.junit.Assert.assertTrue;

public class TestUserInputValidator {

	private UserInputValidator testee = new UserInputValidator();

	@Test(expected = UserInputValidationException.class)
	public void testValidateForNullArgs() {
		//GIVEN
		String[] args = { null, null, null };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test(expected = UserInputValidationException.class)
	public void testValidateForNotValidLengthInput() {
		//GIVEN
		String[] args = { "EUR", "1997-12-01" };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test(expected = UserInputValidationException.class)
	public void testValidateForNotValidCurrencyCode() {
		//GIVEN
		String[] args = { "EU", "1997-12-01", "1998-12-01" };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test(expected = UserInputValidationException.class)
	public void testValidateForTheSameDates() {
		//GIVEN
		String[] args = { "EUR", "1998-12-01", "1998-12-01" };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test(expected = UserInputValidationException.class)
	public void testValidateForNotValidEndDateFormat() {
		//GIVEN
		String[] args = { "EUR", "1998-12-01", "01-12-1999" };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test(expected = UserInputValidationException.class)
	public void testValidateForNotValidStartDateFormat() {
		//GIVEN
		String[] args = { "EUR", "01-12-1999", "1998-12-01" };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test(expected = UserInputValidationException.class)
	public void testValidateForStartDateAfterEndDate() {
		//GIVEN
		String[] args = { "EUR", "2000-12-01", "1998-12-01" };
		//WHEN
		testee.validate(args);
		//THEN UserInputValidationException should be thrown
	}

	@Test
	public void testValidateForValidInput() {
		//GIVEN
		String[] args = { "EUR", "1991-12-01", "1998-12-01" };
		//WHEN
		boolean result = testee.validate(args);
		//THEN
		assertTrue(result);
	}
}
