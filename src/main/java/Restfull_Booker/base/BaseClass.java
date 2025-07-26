package Restfull_Booker.base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import Restfull_Booker.utils.ConfigManager;
import io.restassured.RestAssured;

public class BaseClass {
	/*
	 * static { RestAssured.baseURI = "https://restful-booker.herokuapp.com"; }
	 */

	@BeforeClass
	public void setupBaseURI() {
		String baseUri = ConfigManager.get("baseURI");
		RestAssured.baseURI = baseUri;
		System.out.println("🔧 Base URI set to: " + baseUri);
	}

	@AfterClass
	public void tearDown() {
		System.out.println("🧹 Tests finished. You may clean up here if needed.");
		RestAssured.reset();
	}
}