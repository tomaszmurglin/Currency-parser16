package pl.parser.nbp.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import pl.parser.nbp.exception.DataLoadingException;

/**
 * Responsible for buildings URLs.
 *
 * @author tomasz.murglin@gmail.com
 */
public class UrlBuilderService {

	private static final Logger LOGGER = Logger.getLogger(UrlBuilderService.class.getName());
	private static final String PROTOCOL = "http://";
	private static final String HOST = "www.nbp.pl/";
	private static final String CATALOG = "kursy/xml/";
	private static final String RESOURCE_ADRESSES = "dir.txt";
	private static final String FILE_EXTENSION = ".xml";
	private static final String ERROR_MSG = "Could not receive data from the NBP web service";

	public UrlBuilderService() {

	}

//	public Set<String> buildURLs(String startDate, String endDate) {
//		return PROTOCOL + HOST + CATALOG + findResourceAddresses(startDate, endDate) + FILE_EXTENSION;
//	}

	private List<String> loadResourcesNames() throws IOException {
		InputStream in = null;
		try {
			URL url = new URL(PROTOCOL + HOST + CATALOG + RESOURCE_ADRESSES);
			URLConnection con = url.openConnection();
			in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			String temp = "";
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERROR_MSG);
			throw new DataLoadingException(e);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return null;
	}
}
