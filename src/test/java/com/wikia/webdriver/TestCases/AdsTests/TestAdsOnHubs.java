package com.wikia.webdriver.TestCases.AdsTests;

import com.wikia.webdriver.Common.Core.GeoEdge.GeoEdgeProxy;
import com.wikia.webdriver.Common.Core.URLBuilder.UrlBuilder;
import com.wikia.webdriver.Common.DataProvider.AdsDataProvider;
import com.wikia.webdriver.Common.Templates.NewTestTemplate;
import com.wikia.webdriver.PageObjectsFactory.PageObject.AdsBase.AdsBaseObject;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 *
 * @author Bogna 'bognix' Knychala
 */
@Test (
	groups={"Ads_Hubs_Pages", "Ads"}
)
public class TestAdsOnHubs extends NewTestTemplate {

	private String testedPage;

	@Factory(
		dataProviderClass=AdsDataProvider.class,
		dataProvider="hubsPages"
	)
	public TestAdsOnHubs(String wikiName, String path) {
		super();
		urlBuilder = new UrlBuilder(config.getEnv());
		testedPage = urlBuilder.getUrlForPath(wikiName, path);
		if (config.getQS() != null) {
			testedPage = urlBuilder.appendQueryStringToURL(testedPage, config.getQS());
		}
	}

	@GeoEdgeProxy(country="US")
	@Test (
		groups={"Ads_Hubs_Pages_001"}
	)
	public void TestAdsHubsPages_US() throws Exception {
		AdsBaseObject wikiPage = new AdsBaseObject(driver, testedPage);
		wikiPage.verifyHubTopLeaderboard();
	}

	@GeoEdgeProxy(country="IT")
	@Test (
		groups={"Ads_Hubs_Pages_002"}
	)
	public void TestAdsHubsPages_IT() throws Exception {
		AdsBaseObject wikiPage = new AdsBaseObject(driver, testedPage);
		wikiPage.verifyHubTopLeaderboard();
	}


	@GeoEdgeProxy(country="GB")
	@Test (
		groups={"Ads_Hubs_Pages_003"}
	)
	public void TestAdsHubsPages_GB() throws Exception {
		AdsBaseObject wikiPage = new AdsBaseObject(driver, testedPage);
		wikiPage.verifyHubTopLeaderboard();
	}
}
