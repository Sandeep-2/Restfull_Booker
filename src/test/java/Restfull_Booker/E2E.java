package Restfull_Booker;

import Restfull_Booker.base.BaseClass;
import Restfull_Booker.clients.BookingClient;
import Restfull_Booker.models.Booking;
import Restfull_Booker.models.BookingDates;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Epic("Booking API Tests")
@Feature("End-to-End Booking Operations")
public class E2E extends BaseClass {

	BookingClient client = new BookingClient();
	static int bookingId;
	static Booking booking;

	@Test(priority = 0)
	@Story("Health Check")
	@Description("Verifies the API health by calling /ping endpoint.")
	public void checkAPIHealth() {
		Allure.step("Sending GET request to /ping endpoint");
		Response response = client.healthCheck();

		assertEquals(response.getStatusCode(), 201, "❌ API health check failed");
		Allure.step("✅ API is healthy with status code 201");
	}

	@Test(priority = 1)
	@Story("Create Booking")
	@Description("Creates a new booking and validates its creation.")
	public void createBookingTest() {
		Allure.step("Building booking object");
		booking = Booking.builder().firstname("Jim").lastname("Brown").totalprice(222).depositpaid(true)
				.bookingdates(new BookingDates("2025-01-01", "2025-01-10")).additionalneeds("Breakfast").build();

		Allure.step("Sending POST request to create booking");
		Response response = client.createBooking(booking);

		assertEquals(response.getStatusCode(), 200, "❌ Booking creation failed");
		bookingId = response.jsonPath().getInt("bookingid");
		Allure.step("✅ Booking created with ID: " + bookingId);

		Allure.step("Verifying created booking via GET /booking/{id}");
		Response getResponse = client.getBooking(bookingId);
		assertEquals(getResponse.getStatusCode(), 200);
		assertEquals(getResponse.jsonPath().getString("firstname"), "Jim");
	}

	@Test(priority = 2, dependsOnMethods = "createBookingTest")
	@Story("Update Booking")
	@Description("Performs a full update of an existing booking using PUT.")
	public void updateBookingTest() {
		Allure.step("Updating booking name to John White");
		booking.setFirstname("John");
		booking.setLastname("White");

		Allure.step("Sending PUT request to update booking");
		Response response = client.updateBooking(bookingId, booking);
		assertEquals(response.getStatusCode(), 200, "❌ Booking update failed");

		Allure.step("Verifying updated booking data");
		Response getResponse = client.getBooking(bookingId);
		assertEquals(getResponse.getStatusCode(), 200);
		assertEquals(getResponse.jsonPath().getString("firstname"), "John");
		assertEquals(getResponse.jsonPath().getString("lastname"), "White");
	}

	@Test(priority = 3, dependsOnMethods = "updateBookingTest")
	@Story("Partial Update Booking")
	@Description("Performs a partial update of booking using PATCH.")
	public void partialUpdateBookingTest() {
		Allure.step("Changing firstname to Mike via PATCH");
		booking.setFirstname("Mike");

		Response response = client.partialUpdateBooking(bookingId, booking);
		assertEquals(response.getStatusCode(), 200, "❌ Partial update failed");

		Allure.step("Verifying partial update");
		Response getResponse = client.getBooking(bookingId);
		assertEquals(getResponse.getStatusCode(), 200);
		assertEquals(getResponse.jsonPath().getString("firstname"), "Mike");
	}

	@Test(priority = 4, dependsOnMethods = "partialUpdateBookingTest")
	@Story("Delete Booking")
	@Description("Deletes the booking and verifies deletion.")
	public void deleteBookingTest() {
		Allure.step("Sending DELETE request for booking ID: " + bookingId);
		Response response = client.deleteBooking(bookingId);
		assertEquals(response.getStatusCode(), 201, "❌ Deletion failed");

		Allure.step("Verifying booking no longer exists");
		Response getResponse = client.getBooking(bookingId);
		assertEquals(getResponse.getStatusCode(), 404, "❌ Booking still exists after deletion");
	}
}