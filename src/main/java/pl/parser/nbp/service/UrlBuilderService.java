package pl.parser.nbp.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
	private static String RESOURCE_ADRESSES = "dir";
	private static final String FILE_EXTENSION = ".xml";
	private static final String RESOURCE_EXTENSION = ".txt";
	private static final String ERROR_MSG = "Could not receive data from the NBP web service";
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String TABLE_CODE = "c";

	public UrlBuilderService() {

	}

	public Set<String> buildURLs(String startDate, String endDate) {
		Set<String> urls = new HashSet<>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			Date parsedStartDate = sdf.parse(startDate);
			Date parsedEndDate = sdf.parse(endDate);
			LocalDate localStartDate = parsedStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localEndDate = parsedEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate today = LocalDate.now();

			List<String> resourcesNames = loadResourcesNames(localStartDate, localEndDate, today);
			List<String> filteredResourcesNames = filterResourcesNames(localStartDate, localEndDate, resourcesNames);
			for (String filteredResourceName : filteredResourcesNames) {
				urls.add(PROTOCOL + HOST + CATALOG + filteredResourceName + FILE_EXTENSION);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERROR_MSG);
			throw new DataLoadingException(e);
		}
		return urls;
	}

	private List<String> filterResourcesNames(LocalDate localStartDate, LocalDate localEndDate,
			List<String> resourcesNames) {
		for (Iterator<String> iterator = resourcesNames.iterator(); iterator.hasNext(); ) {
			String resourceName = iterator.next();
			String startYearString = Integer.toString(localStartDate.getYear());
			String twoLastNumbersOfStartYear = startYearString.substring(startYearString.length() - 2);
			String endYearString = Integer.toString(localEndDate.getYear());
			String twoLastNumbersOfEndYear = endYearString.substring(endYearString.length() - 2);
			String dateFromResourceName = resourceName.substring(resourceName.length() - 6);
			int monthAndDayNumberFromResourceName = Integer
					.parseInt(dateFromResourceName.substring(dateFromResourceName.length() - 4));

			String startDateMonth = Integer.toString(localStartDate.getMonthValue());
			String startDateDay = Integer.toString(localStartDate.getDayOfMonth());
			int monthAndDayNumberFromStartDate = Integer.parseInt(startDateMonth + startDateDay);

			String endDateMonth = Integer.toString(localEndDate.getMonthValue());
			String endDateDay = Integer.toString(localEndDate.getDayOfMonth());
			int monthAndDayNumberFromEndDate = Integer.parseInt(endDateMonth + endDateDay);

			if (dateFromResourceName.startsWith(twoLastNumbersOfStartYear)
					&& monthAndDayNumberFromResourceName < monthAndDayNumberFromStartDate) {
				iterator.remove();
			}
			if (dateFromResourceName.startsWith(twoLastNumbersOfEndYear)
					&& monthAndDayNumberFromResourceName > monthAndDayNumberFromEndDate) {
				iterator.remove();
			}
			if (!resourceName.startsWith(TABLE_CODE)) {
				try {
					iterator.remove();
				} catch (IllegalStateException e) {
					continue;
				}
			}
		}
		return resourcesNames;
	}

	private List<String> loadResourcesNames(LocalDate localStartDate, LocalDate localEndDate, LocalDate today)
			throws IOException {
		InputStream in = null;
		List<String> recourcesNames = null;
		try {
			int startYear = localStartDate.getYear();
			int endYear = localEndDate.getYear();
			int currentYear = today.getYear();
			if (startYear != currentYear || endYear != currentYear) {
				if (startYear != endYear) {
					recourcesNames = new ArrayList<>();
					for (int i = 0; i <= endYear - startYear; i++) {
						int countedYear = startYear + i;
						URL url = new URL(
								PROTOCOL + HOST + CATALOG + RESOURCE_ADRESSES + countedYear + RESOURCE_EXTENSION);
						loadResourcesNamesAsStrings(url, recourcesNames, in);
					}
					return recourcesNames;
				}
				RESOURCE_ADRESSES = RESOURCE_ADRESSES + startYear;
			}
			URL url = new URL(PROTOCOL + HOST + CATALOG + RESOURCE_ADRESSES + RESOURCE_EXTENSION);
			loadResourcesNamesAsStrings(url, recourcesNames, in);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERROR_MSG);
			throw new DataLoadingException(e);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return recourcesNames;
	}

	private void loadResourcesNamesAsStrings(URL url, List<String> recourcesNames, InputStream in) throws IOException {
		URLConnection con = url.openConnection();
		in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		String[] result = body.split("[\r\n]");
		recourcesNames.addAll(Arrays.asList(result));
		recourcesNames.removeAll(Collections.singleton(""));
	}
}
