package reverse_geocoding_tests;

import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.googleapi.maps.BaseTest;
import com.googleapi.maps.CsvDataProviders;
import com.googleapi.maps.TestDataClass;
import com.googleapi.maps.Utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ReverseGeocodingTests extends Utilities {
	
	/********************************************************************
	 * Method - Method is testing positive scenario for Reverse Geocoding
	 * - Scenario:
	 * 		- test sends request with valid latitude/longitude and API key
	 * 			verify response has expected formatted address and place_id
	 *******************************************************************/
	@Test
	public void positiveReverseTest() {
		log.info("Positive test for Reverse Geocoding");
		
		// executing request with the test parameters
		Response response = getResponse(constanValues.LATLNG,  constanValues.KEY, constanValues.GEOCODE_API, constanValues.REVERSE_GEOCODING);

		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("status", equalTo(constanValues.STATUS_VALUE));

		// verify response fields
		SoftAssert sf = new SoftAssert();

		// verify results' array is present and not empty
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");

		// verify formatted address field in response
		String formatted_address = response.jsonPath().getString(constanValues.FORMATTED_ADDRESS);
		sf.assertTrue(formatted_address.contains(constanValues.FORMATTED_ADDRESS_FOR_REVERSE_VALUE), "formatted address is not as expected");

		// verify place_id field in response
		String place_id = response.jsonPath().getString(constanValues.PLACE_ID);
		sf.assertTrue(place_id.contains(constanValues.PLACE_ID_VALUE), "place_id address is not as expected");

		sf.assertAll();

	}

	/*********************************************************************
	 * Method - Method is testing NEGATIVE scenarios for Reverse Geocoding
	 * - Scenario 1: 
	 * 		- test sends request with invalid characters in latlng parameter
	 * 			verify request is invalid
	 * - Scenario 2: 
	 * 		- test sends request with missing latlng parameter
	 * 			verify request is invalid
	 * - Scenario 3: 
	 * 		- test sends request with invalid API key parameter
	 *  			verify request is denied
	 * - Scenario 4: 
	 *  		- test sends request with missing API key parameter
	 *    			verify request is denied
	 *********************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeReverseGeocodingTest(Map<String, String> testData) {
		
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);
				
		log.info("Starting negative Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());
		
		// executing request with the test parameters
		Response response = getResponse(dataObject.getLatlng().toString(), dataObject.getKey().toString(), constanValues.GEOCODE_API, constanValues.REVERSE_GEOCODING);
		
		//print response's status and error message values
		log.info("***status value: " + response.jsonPath().getString("status"));
		log.info("***error_message value: " + response.jsonPath().getString("error_message"));

		//verify status and error_message fields in response 
		response.then().assertThat().statusCode(dataObject.getExpectedCode())
			.body("status", equalTo(dataObject.getExpectedStatus()))
			.body("error_message", equalTo(dataObject.getExpectedErrorMessage()));
				
	}
}

