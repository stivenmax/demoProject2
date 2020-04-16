package com.AutomationTalks.demoProject2;

import org.testng.annotations.Test;
import com.belatrix.pageobjects.FilterPage;
import com.belatrix.pageobjects.MenuPage;
import com.belatrix.pageobjects.ResultPage;
import com.belatrix.pageobjects.SearchPage;
import static org.testng.Assert.assertTrue;
import org.testng.Assert;


public class SearchProductTest extends AbstractSearchIntegrationTest {

  @Test
  public void OrderByAscendant() throws InterruptedException {
	  
	  MenuPage menuPage = new MenuPage(getDriver(), getWebContextURL());
	  SearchPage searchPage = new SearchPage(getDriver(), null);
	  FilterPage filterPage = new FilterPage(getDriver(), null);
	  ResultPage resultPage = new ResultPage(getDriver(), null);
	  getDriver().manage().window().maximize();
	  
	  try {
		  menuPage.selectLanguage("ENGLISH");
		  searchPage.searchProduct("Shoes");
		  filterPage.selectBrand();
		  filterPage.selectSize();
		  filterPage.selectStatus();
		  resultPage.printResults("ENGLISH");
		  resultPage.sort("ASCENDANT");
		  resultPage.printProducts();
	  
		  //Assertions
		  assertTrue(resultPage.validateOrder("ASCENDANT"));
	  
	  } catch (Exception e) {
			menuPage.printScreenEvidence(getEvidencePath(),
					"OrderByAscendant_FAILED");
			Assert.fail(e.getMessage());

		} finally {
			//getDriver().close();
		}
	}
  
  @Test
  public void OrderByDesendant() throws InterruptedException {
	  
	  MenuPage menuPage = new MenuPage(getDriver(), getWebContextURL());
	  SearchPage searchPage = new SearchPage(getDriver(), null);
	  FilterPage filterPage = new FilterPage(getDriver(), null);
	  ResultPage resultPage = new ResultPage(getDriver(), null);
	  getDriver().manage().window().maximize();
	  
	  try {
		  menuPage.selectLanguage("ENGLISH");
		  searchPage.searchProduct("Shoes");
		  filterPage.selectBrand();
		  filterPage.selectSize();
		  resultPage.printResults("ENGLISH");
		  resultPage.sort("DESCENDANT");
		  resultPage.printProducts();
	  
		  //Assertions
		  assertTrue(resultPage.validateOrder("DESCENDANT"));
	  
	  } catch (Exception e) {
			menuPage.printScreenEvidence(getEvidencePath(),
					"OrderByAscendant_FAILED");
			Assert.fail(e.getMessage());

		} finally {
			//getDriver().close();
		}
	}
}
