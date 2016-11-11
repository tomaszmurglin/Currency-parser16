package pl.parser.nbp.service;

import java.util.Set;

/**
 * Responsible for buildings URLs.
 *
 * @author tomasz.murglin@gmail.com
 */
public class UrlBuilderService {

	private static final String PROTOCOL = "http://";
	private static final String HOST = "www.nbp.pl/";
	private static final String CATALOG = "kursy/xml/";
	private static final String FILE_EXTENSION = ".xml";

	public UrlBuilderService() {

	}

	public Set<String> buildURLs(String startDate, String endDate) {
		return PROTOCOL + HOST + CATALOG + findResourceAddresses(startDate, endDate) + FILE_EXTENSION;
	}

	private String findResourceAddresses(String startDate, String endDate) {

	}
}
