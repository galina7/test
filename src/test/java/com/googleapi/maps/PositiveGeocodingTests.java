package com.googleapi.maps;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PositiveGeocodingTests extends BaseTest {

	@Test
	public void positiveTest() {
		// get response with valid address and key
		Response response = RestAssured.given(spec).get(
				"json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyDt5o3ClJ3ySEaUAprrk2H-3tQY8vkXbC0");
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
