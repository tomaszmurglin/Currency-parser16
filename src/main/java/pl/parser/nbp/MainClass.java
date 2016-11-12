package pl.parser.nbp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.StopWatch;

import pl.parser.nbp.model.ExchangeRateAggregate;
import pl.parser.nbp.service.ExchangeRateCalculationService;
import pl.parser.nbp.service.NbpClientService;
import pl.parser.nbp.service.UrlBuilderService;
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

		//TODO delete
		UrlBuilderService urlBuilderService = new UrlBuilderService();
		urlBuilderService.buildURLs("2004-03-01", "2012-04-01");


		StopWatch stopWatch = StopWatch.createStarted();
		UserInputValidator userInputValidator = new UserInputValidator();
		userInputValidator.validate(args);
		String currencyCode = args[0];
		String startDate = args[1];
		String endDate = args[2];
		NbpClientService nbpClientService = new NbpClientService();
		nbpClientService.loadData(startDate, endDate);
		ExchangeRateCalculationService exchangeRateCalculationService = new ExchangeRateCalculationService();
		BigDecimal averageBuyingRate = exchangeRateCalculationService
				.calculateAverageRates(currencyCode, true);
		BigDecimal standardDeviationForSellingRates = exchangeRateCalculationService
				.calculateStandardDeviationForSellingRates(currencyCode);
		stopWatch.stop();
		LOGGER.log(Level.INFO, "Execution finished successfully. Elapsed time: {}", stopWatch.getTime(MILLISECONDS));
		LOGGER.log(Level.INFO, "Calculated average buying rate: {}", averageBuyingRate);
		LOGGER.log(Level.INFO, "Calculated standard deviation: {}", standardDeviationForSellingRates);
	}
}
