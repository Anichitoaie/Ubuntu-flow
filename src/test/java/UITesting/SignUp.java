package UITesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class SignUp {
    WebDriver driver;
    private String username = "syr";
    private String password = "Password123!";

    @BeforeMethod
    @Parameters({"browser","runMode"})
    public void setUp(String browser, String runMode) throws MalformedURLException {
        if (runMode.equalsIgnoreCase("local")) {
            // Rulare locală
            if (browser.equalsIgnoreCase("chrome")) {
                driver = new ChromeDriver();
            } else if (browser.equalsIgnoreCase("firefox")) {
                driver = new FirefoxDriver();
            } else {
                throw new IllegalArgumentException("Browser neacceptat: " + browser);
            }
        } else if (runMode.equalsIgnoreCase("docker")) {
            // Rulare în Selenium Grid din Docker
            DesiredCapabilities caps = new DesiredCapabilities();
            if (browser.equalsIgnoreCase("chrome")) {
                caps.setBrowserName("chrome");
            } else if (browser.equalsIgnoreCase("firefox")) {
                caps.setBrowserName("firefox");
            } else {
                throw new IllegalArgumentException("Browser neacceptat: " + browser);
            }
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);
        } else {
            throw new IllegalArgumentException("runMode neacceptat: " + runMode);
        }
    }

    @Test(priority = 1)
    public void Login() {
        // Accesează pagina de login
        driver.get("https://the-internet.herokuapp.com/login");

        // Așteaptă până când formularul de login este vizibil
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

        // Completează formularul de login
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        // Click pe butonul Login
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Așteaptă redirecționarea către pagina securizată
        wait.until(ExpectedConditions.urlContains("/secure"));

        // Verifică dacă autentificarea a avut succes
        Assert.assertTrue(driver.getCurrentUrl().contains("/secure"), "Autentificarea a eșuat: URL-ul nu conține '/secure'");

        // Verifică mesajul de succes
        WebElement successMessage = driver.findElement(By.cssSelector(".flash.success"));
        Assert.assertTrue(successMessage.isDisplayed(), "Mesajul de succes nu este afișat");
        Assert.assertTrue(successMessage.getText().contains("You logged into a secure area!"), "Mesajul de succes nu este corect");
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
        }
    }

}