package com.googleapi.maps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@Listeners({ com.googleapi.maps.TestListener.class })
public class BaseTest {

	protected Logger log;
	protected RequestSpecification spec;
	
	@BeforeMethod(alwaysRun = true)
	public void setUP( ITestContext ctx) {
		String testName = ctx.getCurrentXmlTest().getName();
		log = LogManager.getLogger(testName);

		spec = new RequestSpecBuilder().setBaseUri("https://maps.googleapis.com/maps/api/geocode/").build();
	}

}
