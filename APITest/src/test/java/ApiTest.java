package test.java;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    public void testCreateUser() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\"name\": \"John\", \"job\": \"leader\"}";

        Response response = request.body(requestBody).post("/users");
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    public void testUpdateUser() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\"name\": \"John\", \"job\": \"zion resident\"}";

        Response response = request.body(requestBody).put("/users/2");
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("job"), "zion resident");
    }

    @Test
    public void testDeleteUser() {
        RequestSpecification request = RestAssured.given();

        Response response = request.delete("/users/2");
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test
    public void testDelayedResponse() {
        // First, create a new user
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\"name\": \"John\", \"job\": \"leader\"}";

        Response createResponse = request.body(requestBody).post("/users");
        createResponse.then().log().all();

        String userId = createResponse.jsonPath().getString("id");
        Assert.assertNotNull(userId);

        // Then, send a request to the delayed endpoint
        Response delayedResponse = request.get("/users/" + userId + "?delay=5");
        delayedResponse.then().log().all();

        Assert.assertEquals(delayedResponse.getStatusCode(), 200);
        Assert.assertEquals(delayedResponse.jsonPath().getString("data.id"), userId);
    }
}
