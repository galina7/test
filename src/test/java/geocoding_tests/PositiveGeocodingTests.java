package geocoding_tests;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.List;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.googleapi.maps.BaseTest;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PositiveGeocodingTests extends BaseTest {

	@Test
	public void positiveTest() {
		log.info("Positive test for Geocoding");

		// constructing geocoding URL with test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
				.queryParam("address", "1600 Amphitheatre Parkway Mountain View CA")
				.queryParam("key", "AIzaSyDt5o3ClJ3ySEaUAprrk2H-3tQY8vkXbC0")
				.when()
				.get("/maps/api/geocode/{outputFormat}");

		// verify json schema
		File file = new File(
				"src/test/resources/dataproviders/json/positiveTest.json");
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
		
		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("status", equalTo("OK"));

		// verify fields
		SoftAssert sf = new SoftAssert();

		// verify at least 1 result is in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");
		
		// verify formatted address in response
		String formatted_address = response.jsonPath().getString("results.formatted_address");
		sf.assertEquals(formatted_address, "[1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA]", "formatted_address is incorrect");

		// verify location latitude
		String latStr = response.jsonPath().getString("results.geometry.location.lat");
		int lat = Integer.parseInt(latStr.substring(1, latStr.indexOf(".")));
		sf.assertEquals(lat, 37, "location latitude is not as expected");

		// verify location longitude
		String lngStr = response.jsonPath().getString("results.geometry.location.lng");
		int lng = Integer.parseInt(lngStr.substring(1, lngStr.indexOf(".")));
		sf.assertEquals(lng, -122, "location longitude is not as expected");

		sf.assertAll();

	}
}
