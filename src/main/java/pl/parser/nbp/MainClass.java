package pl.parser.nbp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.StopWatch;

import pl.parser.nbp.validation.UserInputValidator;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Main execution class
 *
 * @author tomasz.murglin@gmail.com
 */
public class MainClass {

	private static final Logger LOGGER = Logger.getLogger(MainClass.class.getName());

	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Execution started successfully");
		StopWatch stopWatch = StopWatch.createStarted();
		UserInputValidator userInputValidator = new UserInputValidator();
		userInputValidator.validate(args);
		// TODO get and parse data, compute, print output
		stopWatch.stop();
		LOGGER.log(Level.INFO, "Execution finished successfully. Elapsed time: {}", stopWatch.getTime(MILLISECONDS));
	}
}
