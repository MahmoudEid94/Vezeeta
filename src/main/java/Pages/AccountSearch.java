package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;


import java.time.Duration;
import java.util.Random;

public class AccountSearch {

    private WebDriver driver;
    private By accountName = By.xpath("//span[@id='UserWidgetMenuTrigger']/span");
    private By specialityDropDownButton = By.id("selectSpecialityDropdown");

    private String cssSpecialityNextButton = "div.PaginatedMenustyle__PaginationHeader-sc-1r0r53i-3 div[data-testid='search-bar__dropdown-speciality__paginated-menu__arrow--next']";
    private String cssSpecialityPreviousButton = "div.PaginatedMenustyle__PaginationHeader-sc-1r0r53i-3 div[data-testid='search-bar__dropdown-speciality__paginated-menu__arrow--previous']";

    private By specialitySelectedValue = By.cssSelector("span[data-testid='search-bar__dropdown-speciality__value']");
    private By citySelectedValue = By.cssSelector("span[data-testid='search-bar__dropdown-city__value']");

    private By specialityMenu = By.xpath("//div[@id='generated_envelopeId_speciality']/div/div[2]/span/ul/li");
    private By specialityMenuList = By.xpath("//div[@id='generated_envelopeId_speciality']//ul[@data-testid='search-bar__dropdown-speciality__paginated-menu__list']");

    private By cityDropDownButton = By.id("selectCityyDropdown");
    private By cityMenu = By.xpath("//div[@id='generated_envelopeId_city']/div/div[2]/span/ul/li");
    private By cityMenuList = By.xpath("//div[@id='generated_envelopeId_city']/div/div[2]/span/ul");
    private String cssCityNextButton = "div.PaginatedMenustyle__PaginationHeader-sc-1r0r53i-3 div[data-testid='search-bar__dropdown-city__paginated-menu__arrow--next']";

    private String cssCityPreviousButton = "div.PaginatedMenustyle__PaginationHeader-sc-1r0r53i-3 div[data-testid='search-bar__dropdown-city__paginated-menu__arrow--next']";

    private By areaDropDownButton = By.id("selectAreaDropdown");
    private By areaList = By.xpath("//ul[@data-testid='search-bar__dropdown-area__paginated-menu__list']/li");
    private By searchButton = By.xpath("//div[@data-testid='search-bar__search-button']");

    private By searchByName = By.cssSelector("input[data-testid='search-bar__text-input-doctorNameText__text-input']");


    public AccountSearch(WebDriver driver) {
        this.driver = driver;
    }

    public String getAccountName() {
        // Wait until Account Name element appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(accountName));

        // Return account Name
        return driver.findElement(accountName).getText();
    }


    public String checkSelectedSpeciality (){
        return driver.findElement(specialitySelectedValue).getText();
    }
    public String checkSelectedCity(){
        return driver.findElement(citySelectedValue).getText();
    }
    // Use 1: Speciality Menu   2: City Menu
    private void clickNextPage (int menuIdentifier) {
        //Click on next button to check next page of menu the element is not interactable in selenium We need to execute JavaScript code
        // Casting the driver
        JavascriptExecutor executor =(JavascriptExecutor) driver;
        /* Execute the JavaScript code with passing script and the web element
           we used arguments[0] to hold a place for the web element
         */
        switch (menuIdentifier){
            case 1:
            {
                executor.executeScript("arguments[0].click();",driver.findElement(By.cssSelector(cssSpecialityNextButton)));
                break;
            }
            case 2:
            {
                executor.executeScript("arguments[0].click();",driver.findElement(By.cssSelector(cssCityNextButton)));
                break;
            }
        }

    }
    private void clickPreviousPage (int menuIdentifier)  {
        //Click on next button to check next page of menu the element is not interactable in selenium We need to execute JavaScript code
        // Casting the driver
        JavascriptExecutor executor =(JavascriptExecutor) driver;
        /* Execute the JavaScript code with passing script and the web element
           we used arguments[0] to hold a place for the web element
         */
        switch (menuIdentifier){
            case 1:
            {
                executor.executeScript("arguments[0].click();",driver.findElement(By.cssSelector(cssSpecialityPreviousButton)));
                break;
            }
            case 2:
            {
                executor.executeScript("arguments[0].click();",driver.findElement(By.cssSelector(cssCityPreviousButton)));
                break;
            }
        }
    }


    public void selectSpeciality(String requiredSpeciality)  {
        // Initiate flag for repeating
        boolean repeatFlag = true;

        // open the dropdown list
        driver.findElement(specialityDropDownButton).click();

        // Need to wait until all lists of options are visible
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(specialityMenu));
        // looping until find required speciality
        while (repeatFlag) {
            // We need to add waiting time to give Selenium enough time to read the DOM
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(specialityMenu));
            // check if the required speciality appears in the visible list
            if (driver.findElement(specialityMenuList).getText().contains(requiredSpeciality)){

                // select the required speciality
                driver.findElement(By.xpath("//ul[@data-testid='search-bar__dropdown-speciality__paginated-menu__list']/li[contains(text(),'" + requiredSpeciality + "')]")).click();

                // Change flag to stop repeating
                repeatFlag = false;
            } else {
               clickNextPage(1);
                // Need to wait until all lists of options are visible
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(specialityMenu));

            }
        }
    }

    public void selectCity(String requiredCity)  {
        boolean repeatFlag = true;
        driver.findElement(cityDropDownButton).click();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cityMenu));
        while (repeatFlag){
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cityMenu));
            if (driver.findElement(cityMenuList).getText().contains(requiredCity)){
                // select the required speciality
                driver.findElement(By.xpath("//div[@id='generated_envelopeId_city']/div/div[2]/span/ul/li[contains(text(),'" + requiredCity + "')]")).click();

                repeatFlag = false;
            }
            else {
                clickNextPage(2);
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cityMenu));

            }
        }
    }

    public void selectArea (){
        driver.findElement(areaDropDownButton).click();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));

        List<WebElement> areaListItems = driver.findElements(areaList);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(areaList));

        Random randomArea = new Random();

        driver.findElement(By.xpath("//ul[@data-testid='search-bar__dropdown-area__paginated-menu__list']/li["
                +(randomArea.nextInt(1,areaListItems.size()) )+"]")).click();
    }

    public void searchByName(String drName) throws InterruptedException {
        driver.findElement(searchByName).sendKeys(drName);
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(6));
        wait.until(ExpectedConditions.attributeToBe(searchByName,"value",drName));
        Thread.sleep(1500);

    }
    public ResultPage clickSearchButton (){
        driver.findElement(searchButton).click();
        return new ResultPage(driver);
    }



}
