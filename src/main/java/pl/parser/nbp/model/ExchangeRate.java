package pl.parser.nbp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents the exchange rate xml node
 *
 * @author tomasz.murglin@gmail.com
 */
public class ExchangeRate implements Serializable {
	private static final long serialVersionUID = 8935018683817780295L;

	private String currencyName;

	private int currencyConverter;

	/**
	 * In ISO 4217 format.
	 */
	private String currencyCode;

	private BigDecimal buyingRate;

	private BigDecimal sellingRate;

	private ExchangeRate() {

	}

	public String getCurrencyName() {
		return currencyName;
	}

	public int getCurrencyConverter() {
		return currencyConverter;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getBuyingRate() {
		return buyingRate;
	}

	public BigDecimal getSellingRate() {
		return sellingRate;
	}

	// TODO implement hashcode, equals, tostring
}
