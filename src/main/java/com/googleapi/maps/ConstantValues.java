package com.googleapi.maps;

public class ConstantValues {
	
	public static final String GEOCODING = "geo";
	public static final String REVERSE_GEOCODING = "reverse";

	 //URL CONSTANTS
	 public static final String GEOCODE_API = "/maps/api/geocode/";
	 public static final String LATLNG = "37.779283,-122.419247";
	 public static final String KEY =  "AIzaSyDt5o3ClJ3ySEaUAprrk2H-3tQY8vkXbC0";
	 public static final String ADDRESS = "1600 Amphitheatre Parkway Mountain View CA";
	 public static final String STATUS_VALUE = "OK";
	 
	 //RESPONSE FIELDS CONSTANTS with values for Positive test
	 public static final String FORMATTED_ADDRESS = "results.formatted_address";
	 public static final String FORMATTED_ADDRESS_VALUE = "[1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA]";
	 public static final String FORMATTED_ADDRESS_FOR_REVERSE_VALUE = "1 Dr Carlton B Goodlett Pl, San Francisco, CA 94102, USA";
	 public static final String LOCATION_LAT = "results.geometry.location.lat";
	 public static final int LOCATION_LAT_VALUE = 37;
	 public static final String LOCATION_LNG = "results.geometry.location.lng";
	 public static final int LOCATION_LNG_VALUE = -122;
	 public static final String PLACE_ID = "results.place_id";
	 public static final String PLACE_ID_VALUE = "ChIJ64zmf5mAhYARv8m8yB1y00Q";
	 
	 //RESPONSE FIELDS
	 public static final String LOCATION_TYPE= "results[0].geometry.location_type";
	 public static final String STATUS = "status";
	 public static final String ERROR_MESSAGE = "error_message";
	 public static final String RESULTS = "results";
}
