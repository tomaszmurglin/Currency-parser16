package pl.parser.nbp.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the exchange rate xml node
 *
 * @author tomasz.murglin@gmail.com
 */
@XmlRootElement(name = "pozycja" )
@XmlAccessorType(XmlAccessType.FIELD)
public class ExchangeRate implements Serializable {
	private static final long serialVersionUID = 8935018683817780295L;

	@XmlElement(name = "nazwa_waluty")
	private String currencyName;

	@XmlElement(name = "przelicznik")
	private int currencyConverter;

	/**
	 * In ISO 4217 format.
	 */
	@XmlElement(name = "kod_waluty")
	private String currencyCode;

	@XmlElement(name = "kurs_kupna")
	private String buyingRate;

	public String getBuyingRate() {
		return buyingRate;
	}

	public String getSellingRate() {
		return sellingRate;
	}

	@XmlElement(name = "kurs_sprzedazy")
	private String sellingRate;

	public ExchangeRate() {

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

	@Override
	public String toString() {
		return "ExchangeRate{" + "currencyName='" + currencyName + '\'' + ", currencyConverter=" + currencyConverter
				+ ", currencyCode='" + currencyCode + '\'' + ", buyingRate=" + buyingRate + ", sellingRate="
				+ sellingRate + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ExchangeRate that = (ExchangeRate) o;

		if (currencyConverter != that.currencyConverter)
			return false;
		if (currencyName != null ? !currencyName.equals(that.currencyName) : that.currencyName != null)
			return false;
		if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null)
			return false;
		if (buyingRate != null ? !buyingRate.equals(that.buyingRate) : that.buyingRate != null)
			return false;
		return sellingRate != null ? sellingRate.equals(that.sellingRate) : that.sellingRate == null;

	}

	@Override
	public int hashCode() {
		int result = currencyName != null ? currencyName.hashCode() : 0;
		result = 31 * result + currencyConverter;
		result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
		result = 31 * result + (buyingRate != null ? buyingRate.hashCode() : 0);
		result = 31 * result + (sellingRate != null ? sellingRate.hashCode() : 0);
		return result;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public void setCurrencyConverter(int currencyConverter) {
		this.currencyConverter = currencyConverter;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setBuyingRate(String buyingRate) {
		this.buyingRate = buyingRate;
	}

	public void setSellingRate(String sellingRate) {
		this.sellingRate = sellingRate;
	}
}
