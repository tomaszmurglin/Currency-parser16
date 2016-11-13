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
	private static String RESOURCE_ADDRESSES = "dir";
	private static final String FILE_EXTENSION = ".xml";
	private static final String RESOURCE_EXTENSION = ".txt";
	private static final String ERROR_MSG_WS = "Could not receive data from the NBP web service. ";
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
			LOGGER.log(Level.SEVERE, ERROR_MSG_WS + e);
			throw new DataLoadingException(e);
		}
		return urls;
	}

	private List<String> filterResourcesNames(LocalDate localStartDate, LocalDate localEndDate,
			List<String> resourcesNames) {
		for (Iterator<String> iterator = resourcesNames.iterator(); iterator.hasNext(); ) {
			String resourceName = iterator.next();
			if (!resourceName.startsWith(TABLE_CODE)) {
				iterator.remove();
				continue;
			}
			String startYearString = Integer.toString(localStartDate.getYear());
			String twoLastNumbersOfStartYear = startYearString.substring(startYearString.length() - 2);
			String endYearString = Integer.toString(localEndDate.getYear());
			String twoLastNumbersOfEndYear = endYearString.substring(endYearString.length() - 2);
			String dateFromResourceName = resourceName.substring(resourceName.length() - 6);
			String monthAndDayNumberFromResourceNameString = dateFromResourceName
					.substring(dateFromResourceName.length() - 4);
			int monthNumberFromResourceName = Integer.parseInt(monthAndDayNumberFromResourceNameString.substring(0, 2));
			int dayNumberFromResourceName = Integer.parseInt(monthAndDayNumberFromResourceNameString
					.substring(monthAndDayNumberFromResourceNameString.length() - 2));

			String startDateMonth = Integer.toString(localStartDate.getMonthValue());
			String startDateDay = Integer.toString(localStartDate.getDayOfMonth());
			int monthNumberFromStartDate = Integer.parseInt(startDateMonth);
			int dayNumberFromStartDate = Integer.parseInt(startDateDay);

			String endDateMonth = Integer.toString(localEndDate.getMonthValue());
			String endDateDay = Integer.toString(localEndDate.getDayOfMonth());
			int monthNumberFromEndDate = Integer.parseInt(endDateMonth);
			int dayNumberFromEndDate = Integer.parseInt(endDateDay);
			if (dateFromResourceName.startsWith(twoLastNumbersOfStartYear)
					&& monthNumberFromResourceName < monthNumberFromStartDate) {
				iterator.remove();
				continue;
			}
			if (dateFromResourceName.startsWith(twoLastNumbersOfStartYear)
					&& monthNumberFromResourceName == monthNumberFromStartDate
					&& dayNumberFromResourceName < dayNumberFromStartDate) {
				iterator.remove();
				continue;
			}
			if (dateFromResourceName.startsWith(twoLastNumbersOfEndYear)
					&& monthNumberFromResourceName > monthNumberFromEndDate) {
				iterator.remove();
				continue;
			}
			if (dateFromResourceName.startsWith(twoLastNumbersOfEndYear)
					&& monthNumberFromResourceName == monthNumberFromEndDate
					&& dayNumberFromResourceName > dayNumberFromEndDate) {
				iterator.remove();
			}
		}
		return resourcesNames;
	}

	private List<String> loadResourcesNames(LocalDate localStartDate, LocalDate localEndDate, LocalDate today)
			throws IOException {
		List<String> resourcesNames = new ArrayList<>();
		try {
			int startYear = localStartDate.getYear();
			int endYear = localEndDate.getYear();
			int currentYear = today.getYear();
			if (startYear != currentYear) {
				if (startYear != endYear) {
					for (int i = 0; i <= endYear - startYear; i++) {
						int countedYear = startYear + i;
						URL url = new URL(
								PROTOCOL + HOST + CATALOG + RESOURCE_ADDRESSES + countedYear + RESOURCE_EXTENSION);
						loadResourcesNamesAsStrings(url, resourcesNames);
					}
					return resourcesNames;
				}
				RESOURCE_ADDRESSES = RESOURCE_ADDRESSES + startYear;
			}
			URL url = new URL(PROTOCOL + HOST + CATALOG + RESOURCE_ADDRESSES + RESOURCE_EXTENSION);
			loadResourcesNamesAsStrings(url, resourcesNames);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERROR_MSG_WS + e);
			throw new DataLoadingException(e);
		}
		return resourcesNames;
	}

	private void loadResourcesNamesAsStrings(URL url, List<String> recourcesNames) throws IOException {
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		String[] result = body.split("[\r\n]");
		recourcesNames.addAll(Arrays.asList(result));
		recourcesNames.removeAll(Collections.singleton(""));
		in.close();
	}
}
