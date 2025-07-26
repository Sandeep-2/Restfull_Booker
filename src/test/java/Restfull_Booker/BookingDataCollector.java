package Restfull_Booker;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import Restfull_Booker.base.BaseClass;
import Restfull_Booker.utils.ConfigManager;
import Restfull_Booker.utils.ExcelWriterUtil;
import io.restassured.response.Response;

public class BookingDataCollector extends BaseClass {

	@Test
	public void collectBookingDataParallel() throws Exception {
		System.out.println("📦 Fetching all booking IDs...");

		Response idResponse = given().get("/booking").then().statusCode(200).extract().response();
		List<Map<String, Object>> bookingIds = idResponse.jsonPath().getList("$");

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<List<Object>>> futures = new ArrayList<>();

		for (Map<String, Object> map : bookingIds) {
			int bookingId = (int) map.get("bookingid");

			futures.add(executor.submit(() -> {
				try {
					Response bookingResponse = given().get("/booking/" + bookingId);
					if (bookingResponse.getStatusCode() != 200)
						return null;

					Map<String, Object> booking = bookingResponse.jsonPath().getMap("$");
					Map<String, String> dates = (Map<String, String>) booking.get("bookingdates");

					return Arrays.asList(bookingId, booking.get("firstname"), booking.get("lastname"),
							booking.get("totalprice"), booking.get("depositpaid"), dates.get("checkin"),
							dates.get("checkout"), booking.get("additionalneeds"));
				} catch (Exception e) {
					System.err.println("⚠️ Failed to fetch booking ID " + bookingId + ": " + e.getMessage());
					return null;
				}
			}));
		}

		executor.shutdown();
		executor.awaitTermination(30, TimeUnit.SECONDS);

		List<List<Object>> rows = new ArrayList<>();
		for (Future<List<Object>> future : futures) {
			List<Object> row = future.get();
			if (row != null)
				rows.add(row);
		}

		System.out.println("✅ Collected " + rows.size() + " bookings.");

		List<String> headers = Arrays.asList("Booking ID", "First Name", "Last Name", "Total Price", "Deposit Paid",
				"Checkin", "Checkout", "Additional Needs");

		String filePath = ConfigManager.get("reportPath");
		ExcelWriterUtil.writeExcel(rows, headers, filePath);
	}
}