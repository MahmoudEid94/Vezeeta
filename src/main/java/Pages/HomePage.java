package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private WebDriver driver;
    private By login =By.id("Header_nav_link_Login--false");
    private By signUp = By.id("Header_button_link_Sign Up--false");

    public HomePage(WebDriver driver){
        this.driver=driver;
    }

    public LoginPage clickLogin (){
        driver.findElement(login).click();
        return new LoginPage(driver);
    }

    public SignUpPage clickSignUp(){
        driver.findElement(signUp).click();
        return new SignUpPage(driver);
    }


}
