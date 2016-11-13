package pl.parser.nbp.service;

import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitTestUrlBuilderService {

	private final UrlBuilderService testee = new UrlBuilderService();

	@Test
	public void testBuildURLs() {
		//GIVEN
		String startDate = "2010-01-06";
		String endDate = "2010-01-08";
		//WHEN
		Set<String> buildedUrls = testee.buildURLs(startDate, endDate);
		//THEN
		assertEquals(3, buildedUrls.size());
	}
}
