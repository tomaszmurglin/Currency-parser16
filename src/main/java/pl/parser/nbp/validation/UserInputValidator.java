package pl.parser.nbp.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.parser.nbp.exception.UserInputValidationException;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Validator for the user input
 *
 * @author tomasz.murglin@gmail.com
 */
public class UserInputValidator {

	private static final Logger LOGGER = Logger.getLogger(UserInputValidator.class.getName());
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final int NUMBER_OF_MANDATORY_INPUT_ARGS = 3;
	private static final int LENGTH_OF_CURRENCY_CODE_BY_ISO_4217 = 3;
	private static final String ERROR_MSG = "Not valid input has been provided. ";
	private static final String VALID_MSG = "User input is valid";
	private static final String CORRECT_INPUT_MSG = "Please make sure your input is correct and try again. Correct input is: [currency_code] [start_date] [end_date] e.g. EUR 2013-01-28 2013-01-31";

	public UserInputValidator() {

	}

	/**
	 * Validates user command line input arguments.
	 *
	 * @param args
	 * 		command line arguments inputted by the user.
	 * @throws UserInputValidationException
	 * 		if it fails.
	 */
	public void validate(@NotNull String[] args) throws UserInputValidationException {
		boolean isUserInputNotValid =
				isInputLengthNotValid(args) || isCurrencyCodeNotValid(args[0]) || areDatesNotValid(args[1], args[2]);
		if (isUserInputNotValid) {
			LOGGER.log(Level.SEVERE, ERROR_MSG);
			throw new UserInputValidationException(ERROR_MSG + CORRECT_INPUT_MSG);
		}
		LOGGER.log(Level.INFO, VALID_MSG);
	}

	private boolean areDatesNotValid(@Nullable String startDate, @Nullable String endDate) {
		if (startDate == null || endDate == null) {
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		sdf.setLenient(false);
		try {
			sdf.parse(startDate);
			sdf.parse(endDate);
		} catch (ParseException e) {
			return true;
		}
		return false;
	}

	private boolean isInputLengthNotValid(String[] args) {
		return args.length != NUMBER_OF_MANDATORY_INPUT_ARGS;
	}

	private boolean isCurrencyCodeNotValid(@Nullable String currencyCode) {
		if (currencyCode == null || currencyCode.length() != LENGTH_OF_CURRENCY_CODE_BY_ISO_4217) {
			return true;
		}
		return false;
	}
}
