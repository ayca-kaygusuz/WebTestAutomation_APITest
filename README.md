# WebTestAutomation_APITest

## Overview

This project contains two main modules: `WebTestAutomation` and `APITest`. The `WebTestAutomation` module focuses on automating web tests for Amazon, while the `APITest` module focuses on testing API endpoints using RestAssured.

## Project Structure

 ├── .gitignore 
 ├── .vscode/ 
 │ └── settings.json 
 ├── APITest/ │ 
 ├── lib/ │ 
 ├── pom.xml 
 │ └── src/ 
 │  └── test/ 
 │      └── java/ 
 │          └── ApiTest.java 
 ├── LICENSE 
 ├── pom.xml 
 ├── README.md 
 └── WebTestAutomation/ 
 ├── bin/ 
 ├── lib/ 
 ├── pom.xml 
 ├── src/
 │  └── test/ 
 │      └── java/ 
 │          └── AmazonTest.java 


## WebTestAutomation Module

### Overview

The `WebTestAutomation` module contains tests for automating web interactions with Amazon. It uses Selenium WebDriver for browser automation and TestNG for test management.

### Key Classes

- [`AmazonTest`](WebTestAutomation/src/test/java/AmazonTest.java): Contains tests for searching and adding products to the cart on Amazon.

### Example Test

```java
@Test
public void testAmazonSearchAndAddToCart() {
    driver.get("https://www.amazon.com");
    Assert.assertTrue(driver.getTitle().contains("Amazon"), "Amazon homepage did not load correctly");
    WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
    searchBox.sendKeys("laptop");
    searchBox.submit();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".s-main-slot")));
    List<WebElement> products = driver.findElements(By.cssSelector(".s-main-slot .s-result-item"));
    for (WebElement product : products) {
        try {
            WebElement price = product.findElement(By.cssSelector(".a-price-whole"));
            WebElement addToCartButton = product.findElement(By.cssSelector(".s-add-to-cart-button"));
            if (price != null && addToCartButton != null) {
                addToCartButton.click();
            }
        } catch (Exception e) {
            // Ignore products that do not have a price or add to cart button
        }
    }
    driver.findElement(By.id("nav-cart")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sc-list-item")));
    List<WebElement> cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
    Assert.assertTrue(cartItems.size() > 0, "No items were added to the cart");
}
```

## APITest Module

### Overview

The `APITest` module contains tests for API endpoints using RestAssured. It includes tests for creating, reading, updating, and deleting users, as well as handling delayed responses.

### Key Classes

- [`APITest`](APITest/src/test/java/ApiTest.java): Contains tests for various API endpoints.

### Example Test
```java
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
```

## Setup
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven
- ChromeDriver and GeckoDriver for Selenium tests

### Installation
1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Maven:
```cmd
mvn clean install
```

## Running Tests
### Web Tests
To run the web tests in the `WebTestAutomation` module, use the following command:
```cmd
mvn test -pl WebTestAutomation
```
### API Tests
To run the API tests in the `APITest` module, use the following command:
```cmd
mvn test -pl APITest
```

## License
This project is licensed under the terms of the GNU General Public License v3.0. See LICENSE tab/file for details.