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

	/****************************************************************
	 * Method - Method is testing positive scenario for Geocoding
	 ***************************************************************/
	@Test
	public void positiveTest() {
		log.info("Positive test for Geocoding");

		// constructing geocoding URL with test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
				.queryParam("address", constanValues.ADDRESS)
				.queryParam("key", constanValues.KEY)
				.when()
				.get(constanValues.GEOCODE_API + "{outputFormat}");

		// verify json schema
		File file = new File(
				"src/test/resources/dataproviders/json/positiveTest.json");
		response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
		
		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("status", equalTo(constanValues.STATUS));

		// verify fields
		SoftAssert sf = new SoftAssert();

		// verify at least 1 result is in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");
		
		// verify formatted address in response
		String formatted_address = response.jsonPath().getString(constanValues.FORMATTED_ADDRESS);
		sf.assertEquals(formatted_address, constanValues.FORMATTED_ADDRESS_VALUE, "formatted_address is incorrect");

		// verify location latitude
		String latStr = response.jsonPath().getString(constanValues.LOCATION_LAT);
		int lat = Integer.parseInt(latStr.substring(1, latStr.indexOf(".")));
		sf.assertEquals(lat, constanValues.LOCATION_LAT_VALUE, "location latitude is not as expected");

		// verify location longitude
		String lngStr = response.jsonPath().getString(constanValues.LOCATION_LNG);
		int lng = Integer.parseInt(lngStr.substring(1, lngStr.indexOf(".")));
		sf.assertEquals(lng, constanValues.LOCATION_LNG_VALUE, "location longitude is not as expected");

		sf.assertAll();

	}
}
