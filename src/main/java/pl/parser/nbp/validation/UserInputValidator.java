package pl.parser.nbp.validation;

import com.sun.istack.internal.NotNull;
import pl.parser.nbp.exception.UserInputValidationException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserInputValidator {

    private static final Logger LOGGER = Logger.getLogger(UserInputValidator.class.getName());
    private static final String ERROR_MSG = "Not valid input has been provided. ";
    private static final String CORRECT_INPUT_MSG = "Please make sure your input is correct and try again. Correct input is: [currency_code] [start_date] [end_date] e.g. EUR 2013-01-28 2013-01-31";

    public void validate(@NotNull String[] args) throws UserInputValidationException {
        if (args.length != 3) {
            LOGGER.log(Level.INFO, ERROR_MSG);
            throw new UserInputValidationException(ERROR_MSG + CORRECT_INPUT_MSG);
        }

    }
}
