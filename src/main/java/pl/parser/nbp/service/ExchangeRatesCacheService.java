package pl.parser.nbp.service;

import java.util.HashSet;
import java.util.Set;

import pl.parser.nbp.model.ExchangeRate;
import pl.parser.nbp.model.ExchangeRateAggregate;

public class ExchangeRatesCacheService {

	private final Set<ExchangeRateAggregate> exchangeRateAggregates = new HashSet<>();

	private ExchangeRatesCacheService() {

	}

	private static ExchangeRatesCacheService INSTANCE;

	public static ExchangeRatesCacheService getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new ExchangeRatesCacheService();
		}
		return INSTANCE;
	}

	public synchronized void removeAllCache() {
		exchangeRateAggregates.clear();
	}

	public synchronized void addToCache(ExchangeRateAggregate exchangeRateAggregate) {
		exchangeRateAggregates.add(exchangeRateAggregate);
	}

	public synchronized Set<ExchangeRateAggregate> getAllCache() {
		return exchangeRateAggregates;
	}

	public synchronized void removeFromCache(ExchangeRate exchangeRate) {
		exchangeRateAggregates.remove(exchangeRate);
	}
}
