package Pages;

import Utilities.Utilities;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.asm.MemberSubstitution;
import org.apache.commons.compress.archivers.zip.ScatterZipOutputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;


public class ResultPage {

    private WebDriver driver;
    private By resultDrCounts = By.cssSelector("span.SortHeaderstyle__SortDesktopDoctors-sc-1ew8wk5-5");
    private By sortMenu = By.id("sort");
    private By priceLTH = By.id("sort-header__radio-button--2--label");
    private By priceHTL = By.id("sort-header__radio-button--3--label");

    private String drCardByName = "//h4[contains(text(),'%s')]";

    private String drCardID = "//div[@data-testid='doctor-card-%d' and not(.//div[@class='DoctorCardSubComponentsstyle__SponsoredText-sc-1vq3h7c-30 VPBCe'])]//span[@data-testid='doctor-card-%d_fees-row_value']";

    private By drsList = By.xpath("//div[contains(@id,'doctor-card') and not(.//div[@class='DoctorCardSubComponentsstyle__SponsoredText-sc-1vq3h7c-30 VPBCe'])]");

    private By pagesList = By.xpath("//div[@class='Paginationstyle__PaginationConatiner-sc-4txdoy-0 cJnBek']/a");

    private String requiredPage = "//div[@class='Paginationstyle__PaginationConatiner-sc-4txdoy-0 " +
            "cJnBek']//div[@id='search-doctors-page__Pagination-page-no--%d']";
    private By drCard = By.xpath("//div[contains(@id,'doctor-card_')]");


    public ResultPage(WebDriver driver) {
        this.driver = driver;
    }

    public int checkCountsOfResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultDrCounts));

        String countText = driver.findElement(resultDrCounts).getText();
        // Extract the numerical part of the string
        // Removes all non-numeric characters
        String numericalPart = countText.replaceAll("\\D+", "");
        // Parse the numerical part to an integer
        return Integer.parseInt(numericalPart);
    }

    public int checkCountsOfDrCards(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultDrCounts));
        return driver.findElements(drCard).size();
    }

    public void sortPricesIncreasing() {
        // Open dropdown menu for sorting
        driver.findElement(sortMenu).click();
        // we need to wait until the DDL open
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(priceLTH));
        // click on required sorting
        driver.findElement(priceLTH).click();
    }

    public void sortPricesDecreasing() {
        // Open dropdown menu for sorting
        driver.findElement(sortMenu).click();
        // we need to wait until the DDL open
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(priceHTL));
        // click on required sorting
        driver.findElement(priceHTL).click();
    }

    private int[] getPricesList() {
        //Get the list of all Dr cards
        List<WebElement> fullDrList = driver.findElements(drsList);

        // Create array to save the prices of each Dr, the size of array based on Dr list
        int[] pricesList = new int[fullDrList.size()];

        /* We need to loop on each Dr card to extract the price
            We used dynamic Xpath based on iteration number
            the iteration number will specify the right element on DOM, so we save the xpath in String variable
            String.format is used to replace the dynamic value i the string of xpath with counter i
            then we applied getText to extract the text from the element, but it is in String type
            We used replace(",", "") to remove special character , from String 1,250 EPG
            We used Integer.parsInt to change the string to int value and saved it in array
        */
        for (int i = 0; i < fullDrList.size(); i++) {
            try {
                driver.findElement(By.xpath(String.format(drCardID, i, i))).isDisplayed();
                pricesList[i] = Integer.parseInt(driver.findElement(By.xpath(String.format(drCardID, i, i))).getText().replace(",", ""));

            } catch (Exception e) {
                System.out.println("No price is visible");
                Utilities.captureScreenshot(driver, "No Price");
                e.printStackTrace();
            }
        }

        return pricesList;
    }

    public boolean isIncreasing() {

        // Get the prices list
        int[] sortedPrices = getPricesList();

        if (sortedPrices.length <= 1) {
            // An array with 0 or 1 elements is always considered sorted.
            return true;
        } else {
            //loop on each price and compare it with the next price
            for (int i = 0; i < sortedPrices.length - 1; i++) {
                // If a larger element is found, the array is not in increasing order.
                if (sortedPrices[i] > sortedPrices[i + 1]) {
                    System.out.println("Prices not ordered correct");
                    System.out.println("Current price is " + sortedPrices[i]);
                    System.out.println("Next price is " + sortedPrices[i + 1]);
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isDecreasing() {

        // Get the prices list
        int[] sortedPrices = getPricesList();

        if (sortedPrices.length <= 1) {
            // An array with 0 or 1 elements is always considered sorted.
            return true;
        } else {
            //loop on each price and compare it with the next price
            for (int i = 0; i < sortedPrices.length - 1; i++) {
                // If a larger element is found, the array is not in decreasing order.
                if (sortedPrices[i] < sortedPrices[i + 1]) {
                    System.out.println("Prices not ordered correct");
                    System.out.println("Current price is " + sortedPrices[i]);
                    System.out.println("Next price is " + sortedPrices[i + 1]);
                    return false;
                }
            }
            return true;
        }
    }

    public boolean loopOnPages(int ordering) throws InterruptedException {

        boolean success = false;

        // Get the last page Number
        List<WebElement> fullPagesList = driver.findElements(pagesList);
        if (!fullPagesList.isEmpty()) {
            int lastPageCount = Integer.parseInt(driver.findElement(By.xpath("//div[@class" +
                    "='Paginationstyle__PaginationConatiner-sc-4txdoy-0 cJnBek']/a[" + (fullPagesList.size() - 1) + "]")).getText());

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));


            // less than 7 pages --> loop on all pages
            if (lastPageCount <= 7 && lastPageCount > 1) {

                for (int i = 2; i <= fullPagesList.size() - 1; i++) {
                    // Wait until page is loaded
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search-doctors" +
                            "-page__Pagination-page-no--1']")));

                    // Scrolling until the pages number is in center of the screen
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                            driver.findElement(By.xpath("//div[@id='search-doctors-page__Pagination-page-no--1']")));

                    // Need to wait until scrolling is done
                    Thread.sleep(750);

                    // Click on required Page
                    driver.findElement(By.xpath(String.format(requiredPage, i))).click();
                    switch (ordering) {
                        case 1: {
                            success = isIncreasing();
                            break;
                        }
                        case 2: {
                            success = isDecreasing();
                            break;
                        }
                    }

                    if (!success) {
                        return false;
                    }


                }
            } else {
                // Loop on first 3 pages
                for (int i = 2; i < 5; i++) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search-doctors" +
                            "-page__Pagination-page-no--1']")));

                    // Scrolling until the pages number is in center of the screen
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                            driver.findElement(By.xpath("//div[@id='search-doctors-page__Pagination-page-no--1']")));

                    // Need to wait until scrolling is done
                    Thread.sleep(1500);

                    // Click on required Page
                    driver.findElement(By.xpath(String.format(requiredPage, i))).click();
                    switch (ordering) {
                        case 1: {
                            success = isIncreasing();
                            break;
                        }
                        case 2: {
                            success = isDecreasing();
                            break;
                        }
                    }

                    if (!success) {
                        return false;
                    }
                }

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search-doctors" +
                        "-page__Pagination-page-no--1']")));
                // Jump to Last Page
                // Scrolling until the pages number is in center of the screen
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                        driver.findElement(By.xpath("//div[@id='search-doctors-page__Pagination-page-no--1']")));

                // Need to wait until scrolling is done
                Thread.sleep(1500);

                // Click on last Page
                driver.findElement(By.xpath(String.format(requiredPage, lastPageCount))).click();
                switch (ordering) {
                    case 1: {
                        success = isIncreasing();
                        break;
                    }
                    case 2: {
                        success = isDecreasing();
                        break;
                    }
                }

                if (!success) {
                    return false;
                }

                // Loop on 3 pages from end
                for (int i = lastPageCount - 1; i > lastPageCount - 5; i--) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='search-doctors" +
                            "-page__Pagination-page-no--1']")));

                    // Scrolling until the pages number is in center of the screen
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                            driver.findElement(By.xpath("//div[@id='search-doctors-page__Pagination-page-no--1']")));

                    // Need to wait until scrolling is done
                    Thread.sleep(1500);

                    // Click on required Page
                    driver.findElement(By.xpath(String.format(requiredPage, i))).click();
                    switch (ordering) {
                        case 1: {
                            success = isIncreasing();
                            break;
                        }
                        case 2: {
                            success = isDecreasing();
                            break;
                        }
                    }

                    if (!success) {
                        return false;
                    }
                }
            }
            return success;
        } else {

            if (checkCountsOfResults()>0){

                switch (ordering) {
                    case 1: {
                        success = isIncreasing();
                        break;
                    }
                    case 2: {
                        success = isDecreasing();
                        break;
                    }
                }
            }
            else {
                System.out.println("No Doctors available, please check the Test Data");
            }

        }
        return success;

    }



    public DrPage choseRandomDr() throws InterruptedException {
        //Get the list of all Dr cards
        List<WebElement> fullDrList = driver.findElements(drsList);

        //Open Random Doctor
        Random random = new Random();
        int randomDrIndex = random.nextInt(0,fullDrList.size());

        System.out.println(fullDrList.get(randomDrIndex).getText());

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                fullDrList.get(randomDrIndex));
        Thread.sleep(1500);

        fullDrList.get(randomDrIndex).click();

        return new DrPage(driver);
    }

    public DrPage openSpecificDr (String drName){
        // Edit the locator with Dr name
        String modifiedDrNameXpath = String.format(drCardByName,drName);

        // Scroll to Dr Card
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
                driver.findElement(By.xpath(modifiedDrNameXpath)));

        // Open Dr page
        driver.findElement(By.xpath(modifiedDrNameXpath)).click();

        return new DrPage(driver);
    }

}
