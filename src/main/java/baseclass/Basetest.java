package baseclass;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Basetest {
	
	//specify objects
	static RequestSpecification reqSpec;
    static ResponseSpecification resSpec;

    //for request setup
    public static RequestSpecification setup() throws FileNotFoundException {
    	
        if (reqSpec == null) {
        	//for logging
            PrintStream log = new PrintStream("src/test/resources/logger/restassured_log.txt");

            //get baseuri
            String baseURI = Configmanager.getProperty("baseURI"); 

            //build request
            reqSpec = new RequestSpecBuilder()
                    .setBaseUri(baseURI)
                    .setContentType("application/x-www-form-urlencoded")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .build();
        }
        return reqSpec;
    }

    //for response setup
    public static ResponseSpecification responseSpec() {
    	
    	//build resspec
        if (resSpec == null) {
            resSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectContentType("application/xml")
                    .build();
        }
        return resSpec;
    }
}
