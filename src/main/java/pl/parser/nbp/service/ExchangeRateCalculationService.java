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
			BigDecimal calculatedAverageBuyingRate = calculateAverageRates(currencyCode, true);
			LOGGER.log(Level.INFO, "Calculated average buying rate: " + calculatedAverageBuyingRate);
			calculateStandardDeviationForSellingRates(currencyCode);
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Could not calculate needed values." + e);
			throw new CalculationException(e);
		}
	}

	public BigDecimal calculateAverageRates(@Nonnull String currencyCode, boolean isBuyingRate) throws ParseException {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getInstance().getAllCache();
		BigDecimal numberOfRecords = BigDecimal.ZERO;
		BigDecimal addedRates = BigDecimal.ZERO;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords.add(new BigDecimal(exchangeRatesFiltered.size()));
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				BigDecimal rate;
				Number number;
				if (isBuyingRate) {
					number = format.parse(exchangeRate.getBuyingRate());
					rate = new BigDecimal(number.toString());
				} else {
					number = format.parse(exchangeRate.getSellingRate());
					rate = new BigDecimal(number.toString());
				}
				addedRates = addedRates.add(rate);
			}
		}
		return addedRates.divide(numberOfRecords).setScale(4, RoundingMode.HALF_UP);
	}

	public double calculateStandardDeviationForSellingRates(@Nonnull String currencyCode) throws ParseException {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getInstance().getAllCache();
		BigDecimal averageSellingRate = calculateAverageRates(currencyCode, false);
		BigDecimal numberOfRecords = BigDecimal.ZERO;
		BigDecimal numerator = BigDecimal.ZERO;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords.add(new BigDecimal(exchangeRatesFiltered.size()));
			Number number;
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				number = format.parse(exchangeRate.getSellingRate());
				BigDecimal sellingRate = new BigDecimal(number.toString());
				double sellingRateSubtractedFromAverage = sellingRate.subtract(averageSellingRate).doubleValue();
				numerator = numerator.add(new BigDecimal(Math.pow(sellingRateSubtractedFromAverage, 2.0)));
			}
		}
		double fraction = numerator.divide((numberOfRecords.subtract(BigDecimal.ONE))).doubleValue();
		double standardDeviationForSellingRates = new BigDecimal(Math.pow(fraction, 0.5))
				.setScale(4, RoundingMode.HALF_UP).doubleValue();
		LOGGER.log(Level.INFO, "Calculated standard deviation for selling rates: " + standardDeviationForSellingRates);
		return standardDeviationForSellingRates;
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
