package pl.parser.nbp.service;

import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import pl.parser.nbp.model.ExchangeRate;
import pl.parser.nbp.model.ExchangeRateAggregate;

/**
 * Service responsible for calculations of the average exchange rate and standard deviation of it.
 *
 * @author tomasz.murglin@gmail.com
 */
public class ExchangeRateCalculationService {

	public ExchangeRateCalculationService() {

	}

	public double calculateAverageRates(@Nonnull String currencyCode, boolean isBuyingRate) {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getINSTANCE().getAllCache();
		int numberOfRecords = 0;
		double addedRates = 0;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords + exchangeRatesFiltered.size();
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				double rate;
				if (isBuyingRate) {
					rate = Double.parseDouble(exchangeRate.getBuyingRate());
				} else {
					rate = Double.parseDouble(exchangeRate.getSellingRate());
				}
				addedRates = addedRates + rate;
			}
		}
		double calculatedAverageRate = addedRates / numberOfRecords;
		return calculatedAverageRate;
	}

	public double calculateStandardDeviationForSellingRates(@Nonnull String currencyCode) {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getINSTANCE().getAllCache();
		double averageSellingRate = calculateAverageRates(currencyCode, false);
		double numberOfRecords = 0;
		double numerator = 0;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords + exchangeRatesFiltered.size();
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				double sellingRate = Double.parseDouble(exchangeRate.getSellingRate());
				double sellingRateSubstracedByAverage = sellingRate - averageSellingRate;
				numerator = numerator + Math.pow(sellingRateSubstracedByAverage, 2.0);
			}
		}
		double fraction = numerator / (numberOfRecords - 1);
		Double result = Math.pow(fraction, 0.5);
		return result;
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> !record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
