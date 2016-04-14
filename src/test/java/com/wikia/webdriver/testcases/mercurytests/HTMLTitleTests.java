package com.wikia.webdriver.testcases.mercurytests;

import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.core.drivers.Browser;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.core.url.Page;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.elements.common.Navigate;
import com.wikia.webdriver.elements.mercury.components.Head;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
public class HTMLTitleTests extends NewTestTemplate {

  /**
   * [0] wikiName
   * [1] path
   * [2] expected title
   */
  private String[][] testCases = {
    {
      "sktest123",
      "/wiki/Sktest123_Wiki",
      "Sktest123 Wiki - Wikia"
    },
    {
      "sktest123",
      "/wiki/Style-5H2",
      "Style-5H2 - Sktest123 Wiki - Wikia"
    },
    {
      "sktest123",
      "/wiki/TestDisplayTitle",
      "testing abc - Sktest123 Wiki - Wikia"
    },
    {
      "sktest123",
      "/wiki/Category:Premium_Videos",
      "Category:Premium Videos - Sktest123 Wiki - Wikia"
    },
    {
      "sktest123",
      "/wiki/Category:Non-premium_Videos",
      "Category:Non-premium Videos - Sktest123 Wiki - Wikia"
    },
    {
      "sktest123",
      "/wiki/Category:Premium",
      "PremiumVideos - Sktest123 Wiki - Wikia"
    },
    {
      "es.pokemon",
      "/wiki/WikiDex",
      "WikiDex - Wikia"
    },
    {
      "es.pokemon",
      "/wiki/Lista_de_Pokémon",
      "Lista de Pokémon - WikiDex - Wikia"
    },
    {
      "es.pokemon",
      "/wiki/Categoría:Regiones",
      "Categoría:Regiones - WikiDex - Wikia"
    },
    {
      "starwars",
      "/wiki/Main_Page",
      "Wookieepedia - Wikia"
    },
    {
      "starwars",
      "/wiki/Droid_starfighter",
      "Droid starfighter - Wookieepedia - Wikia"
    },
    {
      "starwars",
      "/main/category/Future_films",
      "Future_films"
    },
    {
      "starwars",
      "/main/section/Films",
      "Films"
    },
    {
      "mediawiki119",
      "/d/f/203236/latest",
      ""
    },
    {
      "mediawiki119",
      "/d/p/2706859396041803285",
      ""
    },
    {
      "mediawiki119",
      "/d/u/27334045",
      ""
    },
    {
      "dnd4",
      "/wiki/Dungeons_&_Dragons",
      "Dungeons & Dragons - D&D4 Wiki - Wikia"
    }
  };
  
  private Head head;
  private Navigate navigate;

  @BeforeMethod(alwaysRun = true)
  private void init() {
    this.head = new Head();
    this.navigate = new Navigate(driver);
  }

  @Test(groups = "mercury_htmlTitleSet")
  public void mercury_htmlTitleSet() {
    for (String[] testCase : testCases) {
      navigate.toUrl(new Page(testCase[0], testCase[1]).getUrl());
      String actualTitle = head.getDocumentTitle();

      Assertion.assertEquals(actualTitle, getFullTitle(testCase[2]));
    }
  }

  private String getFullTitle(String title) {
    String env = Configuration.getEnv();

    switch (env) {
      case "prod":
        return title;
      case "stable":
        return "stable-s1 - " + title;
      case "preview":
        return "staging-s1 - " + title;
      case "verify":
        return "staging-s2 - " + title;
      default:
        return env + " - " + title;
    }
  }
}