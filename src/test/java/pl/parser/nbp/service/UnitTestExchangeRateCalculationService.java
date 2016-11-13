package pl.parser.nbp.service;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import pl.parser.nbp.model.ExchangeRate;
import pl.parser.nbp.model.ExchangeRateAggregate;

import static org.junit.Assert.assertEquals;

public class UnitTestExchangeRateCalculationService {
	private final ExchangeRateCalculationService testee = new ExchangeRateCalculationService();

	@Test
	public void testCalculateAverageRates() throws ParseException {
		//GIVEN @Before
		//WHEN
		double calculatedAverageBuyingRate = testee.calculateAverageRates("EUR", true);
		//THEN
		assertEquals(2d, calculatedAverageBuyingRate, 0);
	}

	@Test
	public void testCalculateStandardDeviationForSellingRates() throws ParseException {
		//GIVEN @Before
		//WHEN
		double standardDeviationForSellingRates = testee.calculateStandardDeviationForSellingRates("EUR");
		//THEN
		assertEquals(1.4142, standardDeviationForSellingRates, 0);
	}

	@Before
	public void prepareTestData() {
		ExchangeRate exchangeRate1 = new ExchangeRate();
		exchangeRate1.setBuyingRate("1");
		exchangeRate1.setCurrencyCode("EUR");
		exchangeRate1.setCurrencyConverter(1);
		exchangeRate1.setCurrencyName("Euro");
		exchangeRate1.setSellingRate("1");

		ExchangeRate exchangeRate2 = new ExchangeRate();
		exchangeRate2.setBuyingRate("3");
		exchangeRate2.setCurrencyCode("EUR");
		exchangeRate2.setCurrencyConverter(1);
		exchangeRate2.setCurrencyName("Euro");
		exchangeRate2.setSellingRate("3");

		ExchangeRate exchangeRate3 = new ExchangeRate();
		exchangeRate3.setBuyingRate("34");
		exchangeRate3.setCurrencyCode("JAP");
		exchangeRate3.setCurrencyConverter(1);
		exchangeRate3.setCurrencyName("Jen");
		exchangeRate3.setSellingRate("4");

		ExchangeRateAggregate exchangeRateAggregate = new ExchangeRateAggregate();
		Set<ExchangeRate> exchangeRates = new HashSet<>();
		exchangeRates.add(exchangeRate1);
		exchangeRates.add(exchangeRate2);
		exchangeRates.add(exchangeRate3);
		exchangeRateAggregate.setExchangeRates(exchangeRates);

		ExchangeRatesCacheService.getInstance().add(exchangeRateAggregate);
	}
}
