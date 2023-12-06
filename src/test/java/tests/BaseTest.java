package tests;

import Pages.HomePage;
import com.google.common.io.Files;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class BaseTest {

    // We removed private to be able to pass the driver to captureScreenshot method in Utilities
    WebDriver driver;
    protected HomePage homePage;

    @BeforeMethod
    public void setUp(){
        // Use WebDriverManager to set up the ChromeDriver binary
        WebDriverManager.chromedriver().setup();

        // Initialize the ChromeDriver
        driver = new ChromeDriver();

        // Open the Website
        driver.get("https://www.vezeeta.com/en");

        // Maximize the browser window (optional)
        driver.manage().window().maximize();

        homePage = new HomePage(driver);
    }

    @AfterMethod
    public void recordFailure (ITestResult result){
        // Check if the test failed
        if (ITestResult.FAILURE == result.getStatus())
        {
            // casting the driver to takeScreenshot driver
            TakesScreenshot captureScreen = (TakesScreenshot) driver;

            // Capture the screenshot
            File source = captureScreen.getScreenshotAs(OutputType.FILE);

            // Add the date of execution to the screenshot name
            Date currntDate = new Date();
            String fullName = result.getName() +"___"+ currntDate.toString().replace(" ","-").replace(":","-");

            // Define the destination of the screenshot
            String destination = "Resources/Screenshots/" + fullName + ".png";

            // Move the screenshot to our project under resources directory and rename it
            // move method cause IO exception, so we will put it in try catch
            try {
                Files.move(source, new File(destination));
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        //Close the driver when done
          driver.quit();
    }
}
