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

	public BigDecimal calculateAverageBuyingRate(Set<ExchangeRateAggregate> exchangeRateAggregates,
			@NotNull String currencyCode) {
		BigDecimal numberOfRecords = BigDecimal.ZERO;
		BigDecimal addedBuyingRates = BigDecimal.ZERO;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords.add(new BigDecimal(exchangeRatesFiltered.size()));
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				BigDecimal buyingRate = exchangeRate.getBuyingRate();
				addedBuyingRates = addedBuyingRates.add(buyingRate);
			}
		}
		BigDecimal averageBuyingRate = addedBuyingRates.divide(numberOfRecords);
		return averageBuyingRate;
	}

	public BigDecimal calculateStandardDeviationForSellingRates(Set<ExchangeRateAggregate> exchangeRates,
			@NotNull String currencyCode) {
		return null;
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> !record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
