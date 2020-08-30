package geocoding_tests;

import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.googleapi.maps.ConstantValues;
import com.googleapi.maps.CsvDataProviders;
import com.googleapi.maps.TestDataClass;
import com.googleapi.maps.Utilities;

import io.restassured.response.Response;

public class GeocodingTests extends Utilities {
	
	/******************************************************************************************
	 * Method - Method is testing POSITIVE scenario for Geocoding
	 * - Scenario:
	 * 		- test sends request with valid address and API key
	 * 			verify response has expected formatted address, location latitude and longitude
	 ******************************************************************************************/
	@Test
	public void positiveTest() {
		log.info("Positive test for Geocoding");

		// executing request with the test parameters
		Response response = getResponse(ConstantValues.ADDRESS, ConstantValues.KEY, ConstantValues.GEOCODE_API, ConstantValues.GEOCODING);
		
		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body("status", equalTo(ConstantValues.STATUS_VALUE));

		// create SoftAssert object for response verification
		SoftAssert sf = new SoftAssert();

		// verify results array is present and not empty
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");
		
		// verify formatted address is present in response and has expected value
		String formatted_address = response.jsonPath().getString(ConstantValues.FORMATTED_ADDRESS);
		sf.assertEquals(formatted_address, ConstantValues.FORMATTED_ADDRESS_VALUE, "formatted_address is incorrect");

		// verify location latitude is present and has expected value
		String latStr = response.jsonPath().getString(ConstantValues.LOCATION_LAT);
		int lat = Integer.parseInt(latStr.substring(1, latStr.indexOf(".")));
		sf.assertEquals(lat, ConstantValues.LOCATION_LAT_VALUE, "location latitude is not as expected");

		// verify location longitude is present and has expected value
		String lngStr = response.jsonPath().getString(ConstantValues.LOCATION_LNG);
		int lng = Integer.parseInt(lngStr.substring(1, lngStr.indexOf(".")));
		sf.assertEquals(lng, ConstantValues.LOCATION_LNG_VALUE, "location longitude is not as expected");
		
		//print response's formatted_address, location latitude and longitude values
		log.info("***formatted_address : " + formatted_address);
		log.info("***location latitude value: " + latStr);
		log.info("***location longitude value: " + lngStr);

		sf.assertAll();

	}

	/*****************************************************************************
	 * Method - Method is testing NEGATIVE scenarios for Geocoding
	 * - Scenario 1: 
	 * 		- test sends request with missing API key and verify request is denied
	 * - Scenario 2: 
	 * 		- test sends request with invalid API key and verify request is denied
	 *****************************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeInvalidKeyParametersGeocodingTest(Map<String, String> testData) {
		
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);
				
		log.info("Starting negative Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());
				
		// executing request with the test parameters
		Response response = getResponse(dataObject.getAddress().toString(),  dataObject.getKey().toString(), ConstantValues.GEOCODE_API, ConstantValues.GEOCODING);
		
		//print response's status and error message values
		log.info("***status value: " + response.jsonPath().getString(dataObject.getStatus_path()));
		log.info("***error_message value: " + response.jsonPath().getString(dataObject.getError_message_path()));
		
		//verify response's code, status and error message
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
			.body(dataObject.getStatus_path(), equalTo(dataObject.getExpectedStatus()))
			.body(dataObject.getError_message_path(), equalTo(dataObject.getExpectedErrorMessage()));
	}

	
	/***************************************************************
	 * Method - Method is testing NEGATIVE scenarios for  Geocoding
	 * - Scenario 1: 
	 * 		- test sends request with only State as and address
	 *			verify returned location_type value is APPROXIMATE
	 * - Scenario 2: 
	 * 		- test sends request with only Street Name as and address
	 * 			verify returned location_type value is GEOMETRIC_CENTER
	 ****************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeInvalidAddressParametersGeocodingTest(Map<String, String> testData) {
		
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);
		
		log.info("Starting negative Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());
		
		// executing request with the test parameters
		Response response = getResponse(dataObject.getAddress().toString(),  dataObject.getKey().toString(), ConstantValues.GEOCODE_API, ConstantValues.GEOCODING);
		
		//print response's status and location_type values
		log.info("***status value: " + response.jsonPath().getString(ConstantValues.STATUS));
		log.info("***location_type value: " + response.jsonPath().getString(ConstantValues.LOCATION_TYPE));
		
		//verify response status's code, api status and location type
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
			.body(ConstantValues.STATUS, equalTo(dataObject.getExpectedStatus()))
			.body(ConstantValues.LOCATION_TYPE, equalTo(dataObject.getExpectedLocation_Type()));
	}
	
	
	/***************************************************************************************
	 * Method - Method is testing NEGATIVE scenarios for  Geocoding
	 * - Scenario: 
	 *		- test sends request with non-existent address and verify response has 0 results 
	 ***************************************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void emptyResultsTest(Map<String, String> testData) {
		
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);
		
		log.info("Starting empty Results Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());

		// executing request with the test parameters
		Response response = getResponse(dataObject.getAddress().toString(),  dataObject.getKey().toString(), ConstantValues.GEOCODE_API, ConstantValues.GEOCODING);

		//verify response code and status
		response.then().assertThat().statusCode(HttpStatus.SC_OK)
				.body(ConstantValues.STATUS, equalTo(dataObject.getExpectedStatus()));

		// verify response array has 0 elements
		SoftAssert sf = new SoftAssert();
		List<Integer> results = response.jsonPath().getList(ConstantValues.RESULTS);
		sf.assertTrue(results.isEmpty(), "got an empty results");
		
		//print response's status and result array size
		log.info("***status value: " + response.jsonPath().getString(ConstantValues.STATUS));
		log.info("***results array size: " + results.size());

		sf.assertAll();
	}
}
