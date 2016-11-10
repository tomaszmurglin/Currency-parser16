package pl.parser.nbp.service;

import java.util.Set;

import pl.parser.nbp.model.ExchangeRates;

import com.sun.istack.internal.NotNull;

/**
 * Loads data from NBP web service.
 *
 * @author tomasz.murglin@gmail.com
 */
public class NbpClientService {

	public NbpClientService() {

	}

	public Set<ExchangeRates> loadData(@NotNull String currencyCode, @NotNull String startDate,
			@NotNull String endDate) {
		return parseData();
	}

	private Set<ExchangeRates> parseData() {
		ExchangeRateParsingService exchangeRateParsingService = new ExchangeRateParsingService();
		return exchangeRateParsingService.parse();
	}
}
