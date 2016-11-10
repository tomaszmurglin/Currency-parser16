package pl.parser.nbp.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	private static final String VALID_MSG = "User input is valid. Currency code: {}, Start date: {}, End date: {}.";
	private static final String CORRECT_INPUT_MSG = "Please make sure your input is correct and try again. Correct "
			+ "input is: [currency_code in ISO_4217 format] [start_date yyyy-MM-dd] [end_date yyyy-MM-dd] e.g. EUR 2013-01-28 "
			+ "2013-01-31";

	public UserInputValidator() {

	}

	/**
	 * Validates user command line input arguments.
	 *
	 * @param args
	 * 		command line arguments inputted by the user.
	 * @return true if the user input is valid.
	 * @throws UserInputValidationException
	 * 		if is fails.
	 */
	public boolean validate(@NotNull String[] args) throws UserInputValidationException {
		boolean isUserInputNotValid =
				isInputLengthNotValid(args) || isCurrencyCodeNotValid(args[0]) || areDatesNotValid(args[1], args[2]);
		if (isUserInputNotValid) {
			LOGGER.log(Level.SEVERE, ERROR_MSG + CORRECT_INPUT_MSG);
			throw new UserInputValidationException(ERROR_MSG + CORRECT_INPUT_MSG);
		}
		LOGGER.log(Level.INFO, VALID_MSG, args);
		return true;
	}

	private boolean areDatesNotValid(@Nullable String startDate, @Nullable String endDate) {
		if (startDate == null || endDate == null || startDate.equals(endDate)) {
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		sdf.setLenient(false); // to make parsing more strictly
		try {
			Date parsedStartDate = sdf.parse(startDate);
			Date parsedEndDate = sdf.parse(endDate);
			if (!parsedStartDate.before(parsedEndDate)) {
				return true;
			}
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
