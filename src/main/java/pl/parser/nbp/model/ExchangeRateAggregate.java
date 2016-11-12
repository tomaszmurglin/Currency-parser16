package pl.parser.nbp.model;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a set of the exchange rates
 *
 * @author tomasz.murglin@gmail.com
 */
@XmlRootElement(name = "tabela_kursow")
public class ExchangeRateAggregate implements Serializable {
	private static final long serialVersionUID = -2998469714034364623L;

	@XmlElement(name = "pozycja")
	private Set<ExchangeRate> exchangeRates;

	public ExchangeRateAggregate() {

	}

	public Set<ExchangeRate> getExchangeRates() {
		return exchangeRates;
	}

	@Override
	public String toString() {
		return "ExchangeRateAggregate{" + "exchangeRates=" + exchangeRates + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ExchangeRateAggregate that = (ExchangeRateAggregate) o;

		return exchangeRates != null ? exchangeRates.equals(that.exchangeRates) : that.exchangeRates == null;

	}

	@Override
	public int hashCode() {
		return exchangeRates != null ? exchangeRates.hashCode() : 0;
	}
}
