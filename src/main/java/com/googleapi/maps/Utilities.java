package com.googleapi.maps;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Utilities extends BaseTest{

	public Response getResponse(String address, String key, String endpoint, String api) {
		
		String api_param = "";
		if (api == "geo") {
			api_param = "address";
		}else {
			api_param = "latlng";
		}
		
		Response response = RestAssured.given(baseURL).pathParam("outputFormat", "json")
				.queryParam(api_param, address)
				.queryParam("key", key)
				.when()
				.get(endpoint + "{outputFormat}");
		return response;
	}
}
