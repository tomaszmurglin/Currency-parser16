package pl.parser.nbp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

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

	DecimalFormat df = new DecimalFormat("#.####");

	public ExchangeRateCalculationService() {

	}

	public void calculate(@Nonnull String currencyCode) {
		try {
			double calculatedAverageBuyingRate = calculateAverageRates(currencyCode, true);
			LOGGER.log(Level.INFO, "Calculated average buying rate: " + df.format(calculatedAverageBuyingRate));
			calculateStandardDeviationForSellingRates(currencyCode);
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Could not calculate needed values." + e);
			throw new CalculationException(e);
		}
	}

	public double calculateAverageRates(@Nonnull String currencyCode, boolean isBuyingRate) throws ParseException {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getInstance().getAllCache();
		int numberOfRecords = 0;
		double addedRates = 0;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords + exchangeRatesFiltered.size();
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				double rate;
				Number number;
				if (isBuyingRate) {
					number = format.parse(exchangeRate.getBuyingRate());
					rate = number.doubleValue();
				} else {
					number = format.parse(exchangeRate.getSellingRate());
					rate = number.doubleValue();
				}
				addedRates = addedRates + rate;
			}
		}
		return new BigDecimal(addedRates / numberOfRecords).setScale(4, RoundingMode.HALF_UP).doubleValue();
	}

	public double calculateStandardDeviationForSellingRates(@Nonnull String currencyCode) throws ParseException {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getInstance().getAllCache();
		double averageSellingRate = calculateAverageRates(currencyCode, false);
		double numberOfRecords = 0;
		double numerator = 0;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords + exchangeRatesFiltered.size();
			Number number;
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				number = format.parse(exchangeRate.getSellingRate());
				double sellingRate = number.doubleValue();
				double sellingRateSubtractedFromAverage = sellingRate - averageSellingRate;
				numerator = numerator + Math.pow(sellingRateSubtractedFromAverage, 2.0);
			}
		}
		double fraction = numerator / (numberOfRecords - 1);
		double standardDeviationForSellingRates = Math.pow(fraction, 0.5);
		LOGGER.log(Level.INFO,
				"Calculated standard deviation for selling rates: " + df.format(standardDeviationForSellingRates));
		return new BigDecimal(standardDeviationForSellingRates).setScale(4, RoundingMode.HALF_UP).doubleValue();
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
