package pl.parser.nbp.service;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;

import pl.parser.nbp.exception.DataLoadingException;
import pl.parser.nbp.model.ExchangeRateAggregate;

/**
 * Loads data from NBP web service.
 *
 * @author tomasz.murglin@gmail.com
 */
public class NbpClientService {

	private static final Logger LOGGER = Logger.getLogger(NbpClientService.class.getName());
	private static final String ERROR_MSG = "Unable to get XML document with exchange rates from NBP web service.";

	public NbpClientService() {

	}

	public void loadData(@Nonnull String startDate, @Nonnull String endDate) {
		UrlBuilderService urlBuilderService = new UrlBuilderService();
		Set<String> urls = urlBuilderService.buildURLs(startDate, endDate);
		try {
			for (String stringUrl : urls) {
				parseAndSaveExchangeRatesAggregates(stringUrl);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERROR_MSG + e);
			throw new DataLoadingException(e);
		}
	}

	private void parseAndSaveExchangeRatesAggregates(String stringUrl) throws JAXBException, IOException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
			spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(),
					new InputSource(new URL(stringUrl).openStream()));
			JAXBContext jc = JAXBContext.newInstance(ExchangeRateAggregate.class);
			Unmarshaller um = jc.createUnmarshaller();
			ExchangeRateAggregate exchangeRateAggregate = (ExchangeRateAggregate) um.unmarshal(xmlSource);
			ExchangeRatesCacheService.getINSTANCE().addToCache(exchangeRateAggregate);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERROR_MSG + e);
		}
	}
}
