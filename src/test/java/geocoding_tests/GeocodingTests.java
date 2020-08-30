package geocoding_tests;

import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.googleapi.maps.BaseTest;
import com.googleapi.maps.CsvDataProviders;
import com.googleapi.maps.TestDataClass;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GeocodingTests extends BaseTest {

	/****************************************************************
	 * Method - Method is testing POSITIVE scenario for Geocoding
	 ***************************************************************/
	@Test
	public void positiveTest() {
		log.info("Positive test for Geocoding");

		// execute request with provided parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
				.queryParam("address", constanValues.ADDRESS)
				.queryParam("key", constanValues.KEY)
				.when()
				.get(constanValues.GEOCODE_API + "{outputFormat}");

		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("status", equalTo(constanValues.STATUS));


		// verify that results array is presented and not empty
		SoftAssert sf = new SoftAssert();
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");

		// verify formatted address is presented in response and has expected value
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

	/*************************************************************
	 * Method - Method is testing NEGATIVE scenarios for Geocoding
	 * - Scenario1: missing key test
	 * - Scenario2: invalid key test
	 *************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeInvalidKeyParametersGeocodingTest(Map<String, String> testData) {
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);

		log.info("Starting negative Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());

		// executing request with the test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
			.queryParam("address", dataObject.getAddress())
			.queryParam("key", dataObject.getKey())
			.when()
			.get(constanValues.GEOCODE_API + "{outputFormat}");

		log.info("status value:" + response.getBody().jsonPath().getString("status"));
		log.info("error_message value:" + response.getBody().jsonPath().getString("error_message"));

		// verify status and results array is presented and not empty
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
			.body("status", equalTo(dataObject.getExpectedStatus()))
			.body("error_message", equalTo(dataObject.getExpectedErrorMessage()));
	}


	/****************************************************************
	 * Method - Method is testing NEGATIVE scenarios for  Geocoding
	 * - Scenario1: address contains only state (no street, city data)
	 * - Scenario2: address contains only street (no city, state data)
	 ****************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeInvalidAddressParametersGeocodingTest(Map<String, String> testData) {
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);

		log.info("Starting negative Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());

		// executing request with the test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
				.queryParam("address", dataObject.getAddress())
				.queryParam("key", dataObject.getKey()).when()
				.get("/maps/api/geocode/{outputFormat}");

		log.info("status value:" + response.getBody().jsonPath().getString("status"));
		log.info("location_type value:" + response.getBody().jsonPath().getString("results[0].geometry.location_type"));

		//verify response code and status
		//verify that location type has expected value
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
			.body("status", equalTo(dataObject.getExpectedStatus()))
			.body("results[0].geometry.location_type", equalTo(dataObject.getExpectedLocation_Type()));
	}


	/****************************************************************
	 * Method - Method is testing NEGATIVE scenarios for  Geocoding
	 * - Scenario1: non existed address - zero results
	 ****************************************************************/
	@Test
	public void getGeocodingWithNotExistedAddressTest() {
		// get response with non existing address
		// constructing geocoding URL with test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
			.queryParam("address", "123 Main Str FakeCity QQ")
			.queryParam("key", "")
			.when()
			.get("/maps/api/geocode/{outputFormat}");

		// Verify response 200
		Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");

		// verify all fields
		SoftAssert sf = new SoftAssert();

		String status = response.jsonPath().getString("status");
		sf.assertEquals(status, "ZERO_RESULTS", "status is not as expected");

		// verify NO results found in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertTrue(results.isEmpty(), "got an empty results");

		sf.assertAll();
	}
}
