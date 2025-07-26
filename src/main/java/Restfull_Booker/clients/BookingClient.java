package Restfull_Booker.clients;

import Restfull_Booker.config.TokenManager;
import Restfull_Booker.models.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class BookingClient {

	public Response healthCheck() {
		return get("/ping");
	}

	public Response getAllBookingIds() {
		return get("/booking");
	}

	public Response getBooking(int id) {
		return get("/booking/" + id);
	}

	public Response createBooking(Booking booking) {
		return given().contentType(ContentType.JSON).body(booking).post("/booking");
	}

	public Response updateBooking(int id, Booking booking) {
		return given().contentType(ContentType.JSON).cookie("token", TokenManager.getToken()).body(booking)
				.put("/booking/" + id);
	}

	public Response partialUpdateBooking(int id, Booking booking) {
		return given().contentType(ContentType.JSON).cookie("token", TokenManager.getToken()).body(booking)
				.patch("/booking/" + id);
	}

	public Response deleteBooking(int id) {
		return given().cookie("token", TokenManager.getToken()).delete("/booking/" + id);
	}
}