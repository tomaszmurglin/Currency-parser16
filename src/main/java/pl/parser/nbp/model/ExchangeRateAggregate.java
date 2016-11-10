package pl.parser.nbp.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a set of the exchange rates
 *
 * @author tomasz.murglin@gmail.com
 */
public class ExchangeRateAggregate implements Serializable {
	private static final long serialVersionUID = -2998469714034364623L;

	private Set<ExchangeRate> exchangeRates;

	public ExchangeRateAggregate() {

	}

	public Set<ExchangeRate> getExchangeRates() {
		return exchangeRates;
	}

	// TODO implement hashcode, equals, tostring
}
