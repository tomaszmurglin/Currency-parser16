package pl.parser.nbp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import pl.parser.nbp.exception.CalculationException;
import pl.parser.nbp.model.ExchangeRate;
import pl.parser.nbp.model.ExchangeRateAggregate;

/**
 * Service responsible for calculations of the average exchange rate and standard deviation of it.
 *
 * @author tomasz.murglin@gmail.com
 */
public class ExchangeRateCalculationService {

	private static final Logger LOGGER = Logger.getLogger(ExchangeRateCalculationService.class.getName());
	/**
	 * France has like Poland comma as decimal point
	 */
	private final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

	public ExchangeRateCalculationService() {

	}

	public void calculate(@Nonnull String currencyCode) {
		try {
			calculateAverageArithmeticBuyingRate(currencyCode);
			calculateStandardDeviationForSellingRates(currencyCode);
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Could not calculate needed values." + e);
			throw new CalculationException(e);
		}
	}

	public double calculateAverageArithmeticBuyingRate(@Nonnull String currencyCode) throws ParseException {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getInstance().getAllCache();
		DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				descriptiveStatistics.addValue(format.parse(exchangeRate.getBuyingRate()).doubleValue());
			}
		}
		double calculatedAverageBuyingRate = BigDecimal.valueOf(descriptiveStatistics.getMean())
				.setScale(4, RoundingMode.HALF_UP).doubleValue();
		LOGGER.log(Level.INFO, "Calculated average buying rate: " + calculatedAverageBuyingRate);
		return calculatedAverageBuyingRate;
	}

	public double calculateStandardDeviationForSellingRates(@Nonnull String currencyCode) throws ParseException {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getInstance().getAllCache();
		DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				descriptiveStatistics.addValue(format.parse(exchangeRate.getSellingRate()).doubleValue());
			}
		}
		double standardDeviationForSellingRates = BigDecimal.valueOf(descriptiveStatistics.getStandardDeviation())
				.setScale(4, RoundingMode.HALF_UP).doubleValue();
		LOGGER.log(Level.INFO, "Calculated standard deviation for selling rates: " + standardDeviationForSellingRates);
		return standardDeviationForSellingRates;
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
