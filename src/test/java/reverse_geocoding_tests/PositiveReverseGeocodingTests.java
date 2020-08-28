package reverse_geocoding_tests;

import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.googleapi.maps.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PositiveReverseGeocodingTests extends BaseTest {

	/********************************************************************
	 * Method - Method is testing positive scenario for Reverse Geocoding
	 *******************************************************************/
	@Test
	public void positiveReverseTest() {
		log.info("Positive test for Reverse Geocoding");
		
		// executing request with the test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
			.queryParam("latlng", constanValues.LATLNG )
			.queryParam("key", constanValues.KEY)
			.when()
			.get(constanValues.GEOCODE_API + "{outputFormat}");


		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("status", equalTo(constanValues.STATUS));

		// verify response fields
		SoftAssert sf = new SoftAssert();

		// verify at least 1 result is in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");

		// verify formatted address
		String formatted_address = response.jsonPath().getString(constanValues.FORMATTED_ADDRESS);
		sf.assertTrue(formatted_address.contains(constanValues.FORMATTED_ADDRESS_FOR_REVERSE_VALUE), "formatted address is not as expected");

		// verify location longitude
		String place_id = response.jsonPath().getString(constanValues.PLACE_ID);
		sf.assertTrue(place_id.contains(constanValues.PLACE_ID_VALUE), "place_id address is not as expected");

		sf.assertAll();

	}
}
