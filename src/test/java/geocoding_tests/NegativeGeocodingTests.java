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

public class NegativeGeocodingTests extends BaseTest {

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
				
		//verify response code and status
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
		
		// constructing geocoding URL with test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
				.queryParam("address", dataObject.getAddress())
				.queryParam("key", dataObject.getKey()).when()
				.get("/maps/api/geocode/{outputFormat}");
		
		//verify response code and status
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
			.queryParam("key", "AIzaSyDt5o3ClJ3ySEaUAprrk2H-3tQY8vkXbC0")
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
