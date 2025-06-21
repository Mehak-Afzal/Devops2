import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GuestbookTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @Test
    @Order(1)
    public void testPageLoads() {
        driver.get("http://13.48.59.144:8080"); // Adjust if using different port
        assertTrue(driver.getTitle().contains("Guestbook") || driver.getPageSource().contains("Leave a Message"));
    }

    @Test
    @Order(2)
    public void testSubmitValidMessage() {
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("name")).sendKeys("Alice");
        driver.findElement(By.name("email")).sendKeys("alice@example.com");
        driver.findElement(By.name("message")).sendKeys("Hello World!");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertTrue(driver.getPageSource().contains("Hello World!"));
    }

    @Test
    @Order(3)
    public void testEmptyName() {
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("email")).sendKeys("test@example.com");
        driver.findElement(By.name("message")).sendKeys("Missing name test");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        // Form should ideally reject — adjust this test based on actual behavior
        assertFalse(driver.getPageSource().contains("Missing name test"));
    }

    @Test
    @Order(4)
    public void testEmptyMessage() {
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("name")).sendKeys("Bob");
        driver.findElement(By.name("email")).sendKeys("bob@example.com");
        driver.findElement(By.name("message")).sendKeys("");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertFalse(driver.getPageSource().contains("Bob"));
    }

    @Test
    @Order(5)
    public void testLongMessage() {
        String longMsg = "x".repeat(1000);
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("name")).sendKeys("Charlie");
        driver.findElement(By.name("email")).sendKeys("charlie@example.com");
        driver.findElement(By.name("message")).sendKeys(longMsg);
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertTrue(driver.getPageSource().contains("Charlie"));
    }

    @Test
    @Order(6)
    public void testSpecialCharacters() {
        String special = "@#$%^&*()_+!<>";
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("name")).sendKeys("Special");
        driver.findElement(By.name("message")).sendKeys(special);
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertTrue(driver.getPageSource().contains("Special"));
    }

    @Test
    @Order(7)
    public void testMultipleSubmissions() {
        driver.get("http://13.48.59.144:8080");
        for (int i = 0; i < 3; i++) {
            driver.findElement(By.name("name")).sendKeys("User" + i);
            driver.findElement(By.name("message")).sendKeys("Message" + i);
            driver.findElement(By.cssSelector("input[type='submit']")).click();
        }
        assertTrue(driver.getPageSource().contains("User0"));
        assertTrue(driver.getPageSource().contains("User1"));
    }

    @Test
    @Order(8)
    public void testXSSInjection() {
        String xss = "<script>alert('xss');</script>";
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("name")).sendKeys("Attacker");
        driver.findElement(By.name("message")).sendKeys(xss);
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertFalse(driver.getPageSource().contains("<script>"));
    }

    @Test
    @Order(9)
    public void testEmailOptional() {
        driver.get("http://13.48.59.144:8080");
        driver.findElement(By.name("name")).sendKeys("NoEmail");
        driver.findElement(By.name("message")).sendKeys("Testing no email");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertTrue(driver.getPageSource().contains("NoEmail"));
    }

    @Test
    @Order(10)
    public void testPageRefreshPersistence() {
        driver.get("http://13.48.59.144:8080");
        driver.navigate().refresh();
        assertTrue(driver.getPageSource().contains("Messages Stored in Database"));
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
