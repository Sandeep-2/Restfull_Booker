package Restfull_Booker.config;

import Restfull_Booker.models.LoginRequest;
import Restfull_Booker.utils.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class TokenManager {

	private static String token;

	public static String getToken() {
		if (token == null) {
			LoginRequest login = LoginRequest.builder().username(ConfigManager.get("username"))
					.password(ConfigManager.get("password")).build();

			token = RestAssured.given().contentType(ContentType.JSON).body(login).post("/auth").then().statusCode(200)
					.extract().path("token");
		}
		return token;
	}
}