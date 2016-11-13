package pl.parser.nbp.service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.parser.nbp.model.ExchangeRateAggregate;

public class ExchangeRatesCacheService {

	private static final Logger LOGGER = Logger.getLogger(ExchangeRatesCacheService.class.getName());
	private final Set<ExchangeRateAggregate> exchangeRateAggregates = new HashSet<>();

	private ExchangeRatesCacheService() {

	}

	private static ExchangeRatesCacheService INSTANCE;

	public static ExchangeRatesCacheService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExchangeRatesCacheService();
		}
		return INSTANCE;
	}

	public synchronized void add(ExchangeRateAggregate exchangeRateAggregate) {
		exchangeRateAggregates.add(exchangeRateAggregate);
		LOGGER.log(Level.INFO, "Added to cache: " + exchangeRateAggregate);
	}

	public synchronized Set<ExchangeRateAggregate> getAllCache() {
		return exchangeRateAggregates;
	}
}
