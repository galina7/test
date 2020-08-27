package com.googleapi.maps;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class NegativeGeocodingTests extends BaseTest {

	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void negativeMissingParametersGeocodingTest(Map<String, String> testData) {
		// Data
		String no = testData.get("no");
		String json = testData.get("json");
		int statusCode = Integer.parseInt(testData.get("statusCode"));
		String statusResponse = testData.get("status");
		String expectedErrorMessage = testData.get("errorMessage");
		String description = testData.get("description");

		log.info("Starting negative Geocoding Test #" + no + " for " + description);

		// get response with with missing key
		Response response = RestAssured.given(spec).get(json);
		response.print();

		// Verify response 200
		Assert.assertEquals(response.getStatusCode(), statusCode, "Status code is not as expected");

		// verify all fields
		SoftAssert sf = new SoftAssert();

		String status = response.jsonPath().getString("status");
		sf.assertEquals(status, statusResponse, "status is not as expected");

		// verify error message
		String error_message = response.jsonPath().getString("error_message");
		sf.assertTrue(error_message.contains(expectedErrorMessage), "error message is not as expected");

		// verify NO results found in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertTrue(results.isEmpty(), "got an empty results");

		sf.assertAll();
	}

	@Test(dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
	public void getGeocodingWithMissingAddressComponentsTest(Map<String, String> testData) {
		// Data
		String no = testData.get("no");
		String json = testData.get("json");
		int statusCode = Integer.parseInt(testData.get("statusCode"));
		String statusResponse = testData.get("status");
		String location_typeResponse = testData.get("location_type");
		String description = testData.get("description");

		log.info("Starting negative Geocoding Test #" + no + " for " + description);

		// get response with only state in address
		Response response = RestAssured.given(spec).get(json);
		response.print();

		// Verify response 200
		Assert.assertEquals(response.getStatusCode(), statusCode, "Status code is not 200");

		// verify all fields
		SoftAssert sf = new SoftAssert();

		String status = response.jsonPath().getString("status");
		sf.assertEquals(status, statusResponse, "status is not as expected");

		// verify location_type
		String location_type = response.jsonPath().getString("results.geometry.location_type");
		sf.assertEquals(location_type, location_typeResponse, "location type is not as expected");

		// verify at least 1 result is in response
		List<Integer> results = response.jsonPath().getList("results");
		sf.assertFalse(results.isEmpty(), "got an empty results");

		sf.assertAll();
	}

	@Test
	public void getGeocodingWithNotExistedAddressTest() {
		// get response with non existing address
		Response response = RestAssured.given(spec)
				.get("json?address=123+Main+Str+FakeCity+QQ&key=AIzaSyDt5o3ClJ3ySEaUAprrk2H-3tQY8vkXbC0");
		response.print();

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
