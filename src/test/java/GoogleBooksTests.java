import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Year: 2019-20
 *
 * @author Prat3ik on 25/06/19
 * @project Rest-Automation-Example
 */
public class GoogleBooksTests extends Base {
    @Test
    public void verifyTotal10BookDetailsShouldBeReturnedByDefault() {
        String authorNameParam = "q";
        String authorName = "potter";

        Response response = given().param(authorNameParam, authorName).when().get(server.getHost() + URIPathForBooksInformation).
                then().statusCode(200).extract().response();

        // Total items should be greater than 0
        int totalItems = JsonPath.read(response.asString(), "$.totalItems");
        Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

        // Get all books details and verify the size
        List<Object> allBooks = JsonPath.read(response.asString(), "$.items");
        Assert.assertEquals(allBooks.size(), 10, "10 Books should be returned by default");
    }

    @Test
    public void verifyUserCanSetTheMaximumResultsForBooks() {
        String authorNameParam = "q";
        String authorName = "dan+brown";
        String maxResultsParam = "maxResults";
        String maxResults = "25";

        Response response = given().param(authorNameParam, authorName).param(maxResultsParam, maxResults).when().get(server.getHost() + URIPathForBooksInformation).
                then().statusCode(200).extract().response();

        // Total items should be greater than 0
        int totalItems = JsonPath.read(response.asString(), "$.totalItems");
        Assert.assertTrue(totalItems > 0, "Total Items should be greater than 0");

        // Get all books details and verify the size
        List<Object> allBooks = JsonPath.read(response.asString(), "$.items");
        Assert.assertEquals(allBooks.size(), Integer.parseInt(maxResults), maxResults + " books should be returned!");
    }

    @Test
    public void verifyTheErrorWhenBlankAuthorNameSentInRequest() {
        String authorNameParam = "q";

        // Send blank author name in request
        Response response = given().param(authorNameParam, "").when().get(server.getHost() + URIPathForBooksInformation).
                then().statusCode(400).extract().response();

        // Get an error message
        String errorMessage = JsonPath.read(response.asString(), "$.error.message");
        String paramName = JsonPath.read(response.asString(), "$.error.errors[0].location");

        Assert.assertTrue(errorMessage.equals("Missing query.") && paramName.equals(authorNameParam), "'Search Query Parameter Missing' Error should be displayed");
    }

    @Test
    public void verifyThatMoreThan40ResultsShouldNotBeReturned() {
        String authorNameParam = "q";
        String authorName = "india";
        String maxResultsParam = "maxResults";
        String maxResults = "50";

        Response response = given().param(authorNameParam, authorName).param(maxResultsParam, maxResults).
                when().get(server.getHost() + URIPathForBooksInformation).
                then().statusCode(400).extract().response();

        // Get an error message
        String errorMessage = JsonPath.read(response.asString(), "$.error.message");
        String paramName = JsonPath.read(response.asString(), "$.error.errors[0].location");

        Assert.assertTrue(errorMessage.contains("Values must be within the range: [0, 40]") && paramName.equals("maxResults"), "MaxResults invalid range Error should be displayed");
    }
}
