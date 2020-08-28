package reverse_geocoding_tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.googleapi.maps.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PositiveReverseGeocodingTests extends BaseTest {

	@Test
	public void positiveReverseTest() {
		log.info("Positive test for Reverse Geocoding");

		// get response with valid address and key
		Response response = RestAssured.given(baseURL).get(
				"https://maps.googleapis.com/maps/api/geocode/json?latlng=37.779283,-122.4192478&key=AIzaSyDt5o3ClJ3ySEaUAprrk2H-3tQY8vkXbC0");
		response.print();

		// Verify response 200
		Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");

		// verify all fields
		SoftAssert sf = new SoftAssert();

		// verify status
		String status = response.jsonPath().getString("status");
		sf.assertEquals(status, "OK", "status is not as expected");

		// verify at least 1 result is in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");

		// verify formatted address
		String formatted_address = response.jsonPath().getString("results.formatted_address");
		sf.assertTrue(formatted_address.contains("1 Dr Carlton B Goodlett Pl, San Francisco, CA 94102, USA"), "formatted address is not as expected");

		// verify location longitude
		String place_id = response.jsonPath().getString("results.place_id");
		sf.assertTrue(place_id.contains("ChIJ64zmf5mAhYARv8m8yB1y00Q"), "place_id address is not as expected");

		sf.assertAll();

	}
}
