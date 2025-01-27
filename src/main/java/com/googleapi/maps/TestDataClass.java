package com.googleapi.maps;

import java.util.Map;

import org.hamcrest.Matcher;

public class TestDataClass {

	private Object testNumber;
	private Object address;
	private int expectedCode;
	private Object expectedStatus;
	private String expectedLocation_Type;
	private Object description;
	private String expectedErrorMessage;
	private String key;
	private String latlng;
	private String status_path;
	private String error_message_path;

	public TestDataClass(Map<String, String> testData) {
		testNumber = getMapValue(testData, "no");
		address = getMapValue(testData, "address");
		expectedCode = Integer.parseInt(testData.get("statusCode"));
		expectedStatus = getMapValue(testData, "status");
		expectedLocation_Type = getMapValue(testData,"location_type");
		description = getMapValue(testData, "description");
		expectedErrorMessage = getMapValue(testData,"errorMessage");
		key = getMapValue(testData,"key");
		latlng = getMapValue(testData,"latlng");
		status_path = getMapValue(testData,"status_path");
		error_message_path = getMapValue(testData,"error_message_path");
	}
	
	private String getMapValue(Map<String, String> testData, String key) {
		if(testData.containsKey(key)) {
			return testData.get(key);
		} else {
			return "";
		}
	}

	public Object getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(Object testNumber) {
		this.testNumber = testNumber;
	}

	public Object getAddress() {
		return address;
	}

	public void setAddress(Object address) {
		this.address = address;
	}

	public int getExpectedCode() {
		return expectedCode;
	}

	public void setExpectedCode(int expectedCode) {
		this.expectedCode = expectedCode;
	}

	public Object getExpectedStatus() {
		return expectedStatus;
	}

	public void setExpectedStatus(Object expectedStatus) {
		this.expectedStatus = expectedStatus;
	}

	public Object getExpectedLocation_Type() {
		return expectedLocation_Type;
	}

	public void seteEpectedLocation_Type(String expectedLocation_Type) {
		this.expectedLocation_Type = expectedLocation_Type;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public String getExpectedErrorMessage() {
		return expectedErrorMessage;
	}

	public void setExpectedErrorMessage(String expectedErrorMessage) {
		this.expectedErrorMessage = expectedErrorMessage;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLatlng() {
		return latlng;
	}

	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}

	public String getStatus_path() {
		return status_path;
	}

	public void setStatus_path(String status_path) {
		this.status_path = status_path;
	}

	public String getError_message_path() {
		return error_message_path;
	}

	public void setError_message_path(String error_message_path) {
		this.error_message_path = error_message_path;
	}
	
	

}
