package pl.parser.nbp.service;

import java.util.Set;

import javax.annotation.Nonnull;

import pl.parser.nbp.model.ExchangeRateAggregate;

/**
 * Loads data from NBP web service.
 *
 * @author tomasz.murglin@gmail.com
 */
public class NbpClientService {

	public NbpClientService() {

	}

	public Set<ExchangeRateAggregate> loadData(@Nonnull String startDate, @Nonnull String endDate) {
		//		UrlBuilderService urlBuilderService = new UrlBuilderService();
		//		Set<String> urls = urlBuilderService.buildURLs(startDate, endDate);
		//		for (String stringUrl : urls) {
		//			URL url = new URL(stringUrl);
		//			InputStream stream = url.openStream();
		//			Document doc = docBuilder.parse(stream);
		//		}
		return null;
	}
}
