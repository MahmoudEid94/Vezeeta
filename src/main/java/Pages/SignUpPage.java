package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Month;

public class SignUpPage {

    private WebDriver driver;
    private By fullName = By.id("formik-input__input--fullName");
    private By phoneNumber = By.id("phoneNumber");
    private By emailAddress = By.id("formik-input__input--email");
    private By genderFemale = By.id("formik-form__radio-button--female--selector");
    private By genderMale = By.id("formik-form__radio-button--male--selector");
    private By birthDateField = By.id("date-input__input");
    private By yearBackButton = By.xpath("//button[contains(text(),'‹')]");
    private By yearsMatrix = By.className("react-calendar__decade-view__years");
    private By passwordField = By.id("formik-input__input--password");
    private By joinButton = By.xpath("//button[contains(text(),'JOIN NOW')]");
    private By duplicateAccount = By.xpath("//div[contains(text(),'This mobile number is already registered. Please choose another')]");



    public SignUpPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addUserName(String name) {
        driver.findElement(fullName).sendKeys(name);
    }

    public void addPhone(String mobile) {
        driver.findElement(phoneNumber).sendKeys(mobile);
    }

    public void addEmail(String email) {
        driver.findElement(emailAddress).sendKeys(email);
    }

    public void selectGender(char genderChar) {
        switch (genderChar) {
            case 'M':
            case 'm': {
                driver.findElement(genderMale).click();
                break;
            }
            case 'F':
            case 'f': {
                driver.findElement(genderFemale).click();
                break;
            }
        }
    }

    private void findYear(int year) {
        boolean repeat = true;
        //Trigger calender view
        driver.findElement(birthDateField).click();
        // Loop to get required Year
        while (repeat) {

                // Check if the extract of years appears in web element contains the required year
            if (driver.findElement(yearsMatrix).getText().contains(Integer.toString(year))) {
                // Select the required Year
                driver.findElement(By.xpath("//button[contains(text(),'" + year + "')]")).click();
                // Change the flag
                repeat = false;
            }
            else
            {
                // Click the Back button
                driver.findElement(yearBackButton).click();
            }
        }
    }

    public void addBirthday(int day, int month, int year){
        findYear(year);

        // select the xpath of month buttons based on the index of the month
        driver.findElement(By.xpath("//div[@class='react-calendar__year-view__months']/button["+month+"]")).click();

        // select the xpath of day button based on the given day
        driver.findElement(By.xpath("//button[@class='react-calendar__tile react-calendar__month-view__days__day' " +
                "or @class='react-calendar__tile react-calendar__month-view__days__day react-calendar__month-view__days__day--weekend']" +
                "/abbr[text()='"+day+"']")).click();
    }

    public void addPassword(String password){
        driver.findElement(passwordField).sendKeys(password);
    }

    public void submitForm() {
        driver.findElement(joinButton).click();

        // Create object of WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        // Verify the error message is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(duplicateAccount));


    }


    public String checkDuplicate(){
        return driver.findElement(duplicateAccount).getText();
    }




}



