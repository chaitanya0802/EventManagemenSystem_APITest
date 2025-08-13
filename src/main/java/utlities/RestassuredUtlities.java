package utlities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import baseclass.Basetest;
import baseclass.Configmanager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojoclass.Booking;
import pojoclass.Login;

public class RestassuredUtlities {
	
    private static final Logger logger = LogManager.getLogger(RestassuredUtlities.class);
	
	public static String authCode;
	public static String accessToken;
    private RequestSpecification reqSpec;
    public static String bookingId = null;
    public static String trainClassName = null;

	 
   // to initilize reqspec
    public void initialize() {
    	try {
			reqSpec = Basetest.setup();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	logger.info("initialized");

    }
    
    // to get login response
	public Response loginrequest() {
		
		Response response = null;
		
		// Read JSON file and map to POJO list
	    ObjectMapper mapper = new ObjectMapper();
	    List<Login> logins = null;
		try {
			logins = mapper.readValue(
			        new File("src/test/resources/testdata/authdata.json"),
			        new TypeReference<List<Login>>() {}
			);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    for (Login loginData : logins) {
	        // Send request and extract response as JSON string
	        response = RestAssured.given()
	                .spec(reqSpec)
	                .formParam("username", loginData.getUsername())
	                .formParam("password", loginData.getPassword())
	            .when()
	                .post(Configmanager.getProperty("loginresource"))
	            .then()
	                .statusCode(200)
	                .log().all()
	                .extract()
	                .response();

	        //get"auth_code" from JSON
	        authCode = response.jsonPath().getString("auth_code");
	        
	    }
    	logger.info("== called login req");
	        
	    return response;
	}
	
	//for getting accesstoken response
	public Response accessTokenRequest() {
		
		Response response = RestAssured.given()
                .spec(reqSpec)
                .formParam("auth_code",  authCode)
            .when()
            	.post(Configmanager.getProperty("accesstokenresource"))
            .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        //get"auth_code" from JSON
		accessToken = response.jsonPath().getString("access_token");
		
    	logger.info("== called accessTokenRequest");
		
		return response;
	}

	//to view booking list - get request
	public Response viewBookingListRequest() {
		
		Response response = RestAssured.given()
                .spec(reqSpec)
            .when()
            	.get(Configmanager.getProperty("viewbookinglistresource"))
            .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
		
		//get booking id and save to variable
		List<String> bookingIds = response.xmlPath().getList("bookings.booking.bookingId");
		bookingId = bookingIds.get(0);
		
		//get trainClassName and save to variable
		 
		List<String> trainClassNames = response.xmlPath().getList("bookings.booking.trainClass");
		trainClassName = trainClassNames.get(0);
		
    	logger.info("== called viewBookingListRequest");
		
		return response;
	}

	
	//to get booking by id 
	public Response viewBookingByIdRequest() {
		Response response = RestAssured.given()
                .spec(reqSpec)
            .when()
            	.get(Configmanager.getProperty("viewbookingbyidresource")+bookingId)
            .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
		
    	logger.info("== called viewBookingByIdRequest");
		
		return response;
	}

	//to get booking by train class
	public Response viewBookingByTrainClassRequest() {
		
		Response response = RestAssured.given()
                .spec(reqSpec)
            .when()
            	.get(Configmanager.getProperty("viewbookingbytrainclassresource")+"?trainClass="+trainClassName)
            .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
		
    	logger.info("== called viewBookingByTrainClassRequest");
		
		return response;
		
	}
	
	//to add booking 
	public Response addBookingRequest() {
	    Response response = null;

	    try {
	        // Read JSON file into POJO list
	        ObjectMapper mapper = new ObjectMapper();
	        List<Booking> bookings = mapper.readValue(
	                new File("src/test/resources/testdata/bookingdata.json"),
	                new TypeReference<List<Booking>>() {}
	        );

	        for (Booking booking : bookings) {
	            response = RestAssured.given()
	                    .spec(reqSpec)
		                    .formParam("bookingId", booking.getBookingId())
		                    .formParam("bookingDate", booking.getBookingDate())
		                    .formParam("departCity", booking.getDepartCity())
		                    .formParam("arrivalCity", booking.getArrivalCity())
		                    .formParam("passengerCount", booking.getPassengerCount())
		                    .formParam("trainName", booking.getTrainName())
		                    .formParam("passengerName", booking.getPassengerName())
		                    .formParam("trainClass", booking.getTrainClass())
		                    .formParam("ticketType", booking.getTicketType())
	                    .when()
		                	.post(Configmanager.getProperty("addbookingresource"))
	                    .then()
		                    .statusCode(200).log().all()
		                    .extract()
		                    .response();
	        }

	    } catch (IOException e) {
	        throw new RuntimeException("Failed to read booking JSON file", e);
	    }

    	logger.info("== called addBookingRequest");
	    
	    return response;
	}


	//to delete booking
	public Response deleteBookingByidRequest() {
		
		Response response = RestAssured.given()
                .spec(reqSpec)
            .when()
            	.delete(Configmanager.getProperty("deletebookingbyidresource")+"/1001")
            .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
		
    	logger.info("== called deleteBookingByidRequest");

		return response;
	}
	

}

