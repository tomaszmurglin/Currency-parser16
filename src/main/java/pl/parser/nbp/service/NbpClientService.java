package pl.parser.nbp.service;

import java.util.Set;

import pl.parser.nbp.model.ExchangeRateAggregate;

import com.sun.istack.internal.NotNull;

/**
 * Loads data from NBP web service.
 *
 * @author tomasz.murglin@gmail.com
 */
public class NbpClientService {

	public NbpClientService() {

	}

	public Set<ExchangeRateAggregate> loadData(@NotNull String currencyCode, @NotNull String startDate,
			@NotNull String endDate) {
		return parseData();
	}

	private Set<ExchangeRateAggregate> parseData() {
		ExchangeRateParsingService exchangeRateParsingService = new ExchangeRateParsingService();
		return exchangeRateParsingService.parse();
	}
}
