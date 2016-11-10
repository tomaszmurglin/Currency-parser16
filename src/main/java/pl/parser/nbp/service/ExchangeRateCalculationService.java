package pl.parser.nbp.service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import pl.parser.nbp.model.ExchangeRate;
import pl.parser.nbp.model.ExchangeRateAggregate;

import com.sun.istack.internal.NotNull;

/**
 * Service responsible for calculations of the average exchange rate and standard deviation of it.
 *
 * @author tomasz.murglin@gmail.com
 */
public class ExchangeRateCalculationService {

	public ExchangeRateCalculationService() {

	}

	public BigDecimal calculateAverageRates(Set<ExchangeRateAggregate> exchangeRateAggregates,
			@NotNull String currencyCode, boolean isBuyingRate) {
		BigDecimal numberOfRecords = BigDecimal.ZERO;
		BigDecimal addedRates = BigDecimal.ZERO;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords.add(new BigDecimal(exchangeRatesFiltered.size()));
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				BigDecimal rate;
				if (isBuyingRate) {
					rate = exchangeRate.getBuyingRate();
				} else {
					rate = exchangeRate.getSellingRate();
				}
				addedRates = addedRates.add(rate);
			}
		}
		BigDecimal calculatedAverageRate = addedRates.divide(numberOfRecords);
		return calculatedAverageRate;
	}

	public BigDecimal calculateStandardDeviationForSellingRates(Set<ExchangeRateAggregate> exchangeRates,
			@NotNull String currencyCode) {
		BigDecimal averageSellingRate = calculateAverageRates(exchangeRates, currencyCode, false);
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> !record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
