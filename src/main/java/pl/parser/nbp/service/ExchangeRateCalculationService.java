package pl.parser.nbp.service;

import java.math.BigDecimal;
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

	public BigDecimal calculateAverageRates(@Nonnull String currencyCode, boolean isBuyingRate) {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getINSTANCE().getAllCache();
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

	public BigDecimal calculateStandardDeviationForSellingRates(@Nonnull String currencyCode) {
		Set<ExchangeRateAggregate> exchangeRateAggregates = ExchangeRatesCacheService.getINSTANCE().getAllCache();
		BigDecimal averageSellingRate = calculateAverageRates(currencyCode, false);
		BigDecimal numberOfRecords = BigDecimal.ZERO;
		BigDecimal numerator = BigDecimal.ZERO;
		for (ExchangeRateAggregate exchangeRateAggregate : exchangeRateAggregates) {
			Set<ExchangeRate> exchangeRates = exchangeRateAggregate.getExchangeRates();
			Set<ExchangeRate> exchangeRatesFiltered = filterExchangeRateByCurrencyCode(exchangeRates, currencyCode);
			numberOfRecords = numberOfRecords.add(new BigDecimal(exchangeRatesFiltered.size()));
			for (ExchangeRate exchangeRate : exchangeRatesFiltered) {
				BigDecimal sellingRate = exchangeRate.getSellingRate();
				BigDecimal sellingRateSubstracedByAverage = sellingRate.subtract(averageSellingRate);
				numerator = numerator.add((sellingRateSubstracedByAverage.pow(2)));
			}
		}
		BigDecimal fraction = numerator.divide(numberOfRecords.subtract(BigDecimal.ONE));
		//		Double result = fraction.pow(0.5);
		//		return result;
		return null;
	}

	private Set<ExchangeRate> filterExchangeRateByCurrencyCode(Set<ExchangeRate> exchangeRates, String currencyCode) {
		return exchangeRates.stream().filter(record -> !record.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toSet());
	}
}
