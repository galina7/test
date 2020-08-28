package com.googleapi.maps;

import java.util.Map;

public class TestDataClass {

	private Object testNumber;
	private Object address;
	private Object expectedCode;
	private Object expectedStatus;
	private String expectedLocation_Type;
	private Object description;
	private String expectedErrorMessage;
	private String key;

	public TestDataClass(Map<String, String> testData) {
		testNumber = getMapValue(testData, "no");
		address = getMapValue(testData, "address");
		expectedCode = getMapValue(testData, "statusCode");
		expectedStatus = getMapValue(testData, "status");
		expectedLocation_Type = getMapValue(testData,"location_type");
		description = getMapValue(testData, "description");
		expectedErrorMessage = getMapValue(testData,"errorMessage");
		key = getMapValue(testData,"key");
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

	protected Object getExpectedCode() {
		return expectedCode;
	}

	public void setExpectedCode(String expectedCode) {
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
	
	

}
