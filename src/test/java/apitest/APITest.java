package apitest;

import org.testng.annotations.Test;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;
import utlities.ExtentTestNGListener;
import utlities.RestassuredUtlities;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

//add listener
@Listeners({ ExtentTestNGListener.class })
public class APITest {

    // get instance
    RestassuredUtlities utils = new RestassuredUtlities();

    @BeforeTest
    public void beforeTest() {
        utils.initialize();

        System.out.println("Started");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("Ended");
    }

    @Test(priority = 1)
    public void login() {
        // send request
        Response res = utils.loginrequest();

        // Assert 200 OK and auth_code property
        assertThat(res.getStatusCode(), equalTo(200));
        assertThat(res.jsonPath().getMap("$"), hasKey("auth_code"));
    }

    @Test(priority = 2)
    public void getAccessToken() {
        // send request
        Response res = utils.accessTokenRequest();

        // Assert 200 OK and auth_code property
        assertThat(res.getStatusCode(), equalTo(200));
        assertThat(res.jsonPath().getMap("$"), hasKey("access_token"));
    }

    @Test(priority = 3)
    public void viewBookingList() {
        Response res = utils.viewBookingListRequest();
        List<String> bookingIds = res.xmlPath().getList("bookings.booking.bookingId");

        // Assert 200 OK and bookingIds list is not empty
        assertThat(res.getStatusCode(), equalTo(200));
        assertThat("Booking ID list should not be empty", bookingIds, is(not(empty())));
    }

    @Test(dependsOnMethods = { "viewBookingList" })
    public void viewBokingById() {
        Response res = utils.viewBookingByIdRequest();

        // Assert 200 OK and valid bookingId
        assertThat(res.getStatusCode(), equalTo(200));

        String bookingId = res.xmlPath().getString("booking.bookingId");
        // Assert bookingId is equal to previous req
        assertThat("Booking ID should be valid", bookingId, equalTo(RestassuredUtlities.bookingId));
    }

    @Test(dependsOnMethods = { "viewBookingList" })
    public void viewBokingByTrainClass() {
        Response res = utils.viewBookingByTrainClassRequest();

        // Assert 200 OK and valid trainclass
        assertThat(res.getStatusCode(), equalTo(200));

        String trainClass = res.xmlPath().getString("booking.trainClass");
        // Assert bookingId is equal to previous req
        assertThat("trainClass should be valid", trainClass, equalTo(RestassuredUtlities.trainClassName));
    }

    @Test(priority = 4)
    public void addBooking() {
        Response res = utils.addBookingRequest();

        // Assert 200 OK and added booking
        assertThat(res.getStatusCode(), equalTo(200));

        List<String> bookingIds = res.xmlPath().getList("bookings.booking.bookingId");
        boolean found = false;
        for (String id : bookingIds) {
            if (id.equals("15")) {
                found = true;
                break;
            }
        }

        // Assert that bookingId 15 exists
        assertThat("Booking ID 15 should be present in the list", found, equalTo(true));

    }

    @Test(priority = 5)
    public void deleteBookingByid() {
        Response res = utils.deleteBookingByidRequest();

        // Assert 200 OK and deleted booking
        assertThat(res.getStatusCode(), equalTo(200));

        List<String> bookingIds = res.xmlPath().getList("bookings.booking.bookingId");
        boolean deleted = false;
        for (String id : bookingIds) {
            if (id.equals("1001")) {
                deleted = true;
                break;
            }
        }

        // Assert that bookingId 15 exists
        assertThat("Booking ID should be deleted in the list", deleted, equalTo(false));
    }

}
