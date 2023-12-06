package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    private String savedUserName ="01221604554";
    private String savedPassowrd ="Mahmoud94";
    private By userNameField = By.id("formik-input__input--email");
    private By passwordField = By.id("formik-input__input--password");
    private By loginButton = By.xpath("//button[contains(text(),'LOGIN')]");
    private By incorrectMessage = By.xpath("//div[contains(text(),'Mobile number/Email or password is incorrect. please try again')]");
    private By emptyPassword = By.xpath("//div[@id='formik-input__input-wrapper--password']/div");
    private By invalidMobile = By.xpath("//div[@id='telephone-input__input-wrapper']/div[contains(text(),'Mobile Number is invalid')]");

    public LoginPage (WebDriver driver){
        this.driver=driver;
    }

    public AccountSearch completeLogin(){
        addUserName(savedUserName);
        addPassword(savedPassowrd);
        driver.findElement(loginButton).click();
        return new AccountSearch(driver);
    }


    public void addUserName (String userName){
        driver.findElement(userNameField).sendKeys(userName);
    }


    public void addPassword (String password){
        driver.findElement(passwordField).sendKeys(password);
    }

    public AccountSearch clickLoginButton (){
        driver.findElement(loginButton).click();
        return new AccountSearch(driver);
    }

    public boolean verifyWrongLogin(){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(incorrectMessage));
       return driver.findElement(incorrectMessage).isDisplayed();
    }

    public boolean verifyEmptyPassword(){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyPassword));
        return driver.findElement(emptyPassword).isDisplayed();
    }

    public boolean verifyInvalidMobile(){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(invalidMobile));
        return driver.findElement(invalidMobile).isDisplayed();
    }



}
