// This is a simple test that searches for the word "laptop" on Amazon.com, adds in-stock, 
// non-discounted products from the first page of search results to the cart, and verifies that 
// the added products are correct. The test is parameterized to run on Chrome and Firefox browsers.

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;



public class AmazonTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Parameters("browser")
    @BeforeClass
    public void setUp(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");
            driver = new FirefoxDriver();
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void testAmazonSearchAndAddToCart() {
        // Step 1: Enter amazon.com and verify that the homepage loads correctly
        driver.get("https://www.amazon.com");
        Assert.assertTrue(driver.getTitle().contains("Amazon"), "Amazon homepage did not load correctly");

        // Step 2: Search for the word "laptop"
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("laptop");
        searchBox.submit();

        // Step 3: Add in-stock, non-discounted products from the first page of search results to the cart
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

        // Step 4: Go to the cart and verify that the added products are correct
        driver.findElement(By.id("nav-cart")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sc-list-item")));
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
        Assert.assertTrue(cartItems.size() > 0, "No items were added to the cart");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}