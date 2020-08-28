package reverse_geocoding_tests;

import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import com.googleapi.maps.BaseTest;
import com.googleapi.maps.CsvDataProviders;
import com.googleapi.maps.TestDataClass;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class NegativeReverseGeocodingTests extends BaseTest {

	/*********************************************************************
	 * Method - Method is testing NEGATIVE scenarios for Reverse Geocoding
	 * - Scenario1: invalid characters in latlng parameter
	 * - Scenario2: missing latlng parameter
	 * - Scenario3: invalid key parameters
	 * - Scenario4: missing key parameters
	 *********************************************************************/
	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeReverseGeocodingTest(Map<String, String> testData) {
		
		// Create data object with test parameters
		TestDataClass dataObject = new TestDataClass(testData);
				
		log.info("Starting negative Geocoding Test #" + dataObject.getTestNumber() + " for " + dataObject.getDescription());
		
		// executing request with the test parameters
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
			.queryParam("latlng", dataObject.getLatlng())
			.queryParam("key", dataObject.getKey())
			.when()
			.get("/maps/api/geocode/{outputFormat}");

		//verify response fields
		response.then().assertThat().statusCode(dataObject.getExpectedCode())
			.body("status", equalTo(dataObject.getExpectedStatus()))
			.body("error_message", equalTo(dataObject.getExpectedErrorMessage()));
				
	}
}

