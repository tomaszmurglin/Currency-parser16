package pl.parser.nbp.service;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import org.w3c.dom.Document;

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

	public Set<ExchangeRateAggregate> loadData(@NotNull String startDate, @NotNull String endDate) {
		UrlBuilderService urlBuilderService = new UrlBuilderService();
		Set<String> urls = urlBuilderService.buildURLs(startDate, endDate);
		for (String stringUrl : urls) {
			URL url = new URL(stringUrl);
			InputStream stream = url.openStream();
			Document doc = docBuilder.parse(stream);
		}
		return null;
	}
}
