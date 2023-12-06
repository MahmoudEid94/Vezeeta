package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DrPage {

    private WebDriver driver;
    private By drNameField = By.xpath("//h3[contains(text(),'Doctor')]/span");
    private By nextDayButton = By.id("nav-arrow");
    private By moreButton = By.xpath("//span[@data-testid='toggle-expand-day']");
    private By calendarView = By.xpath("//div[@data-testid='schedule-container']");
    private By cancelBookingButton = By.xpath("//div[contains(@class,'ReservationFormstyle__Container-sc')]//button[@data-testid='reservation-form__cancel-button']");

    // Replace by DD/MM/YYYY
    private String requiredDaySlots= "//div[contains(@data-autotestid,'schedule-day-%02d/%02d/%d__slots-container')]";
    private String requiredDaySlotsType = "//div[@data-autotestid='schedule-day--%02d/%02d/%d--available--true']/div" +
            "[2" +
            "]/div";
    private String timeSlots = "//div[@data-autotestid='schedule-day-%02d/%02d/%d__slots-container']/span";

    private String availableRequiredDay = "//div[@data-autotestid='schedule-day--%02d/%02d/%d--available--true']/div";

    // Replace %d with hour and %02d with minutes in integer format without leading Zero,
    // for example min:5 will be 05
    private String requiredTimeSlot = "//div[@data-autotestid='schedule-day-%02d/%02d/%d__slots-container']/span" +
            "[@data" +
            "-testid='undefined__slot--%d:%02d PM']";

    public DrPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getDrName(){
        return driver.findElement(drNameField).getText();
    }

    /**
     * This function is searching for the required day.
     * @param day
     * @param month
     * @param year
     */
    private Boolean checkAvailabilityRequiredDay(int day, int month, int year) throws InterruptedException {
        // Wait until the calendar view is present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.presenceOfElementLocated(calendarView));

        // Get today's date
        LocalDate today = LocalDate.now();

        // Parse the provided date
        String providedDate = String.format("%02d/%02d/%d",day,month,year); // Replace this with your provided date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(providedDate, formatter);

        // Calculate the difference in days
        int daysDifference = (int) ChronoUnit.DAYS.between(today, parsedDate);

        // loop until required day present
        for (int i=0 ; i < daysDifference/3; i++ ){
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",
                    driver.findElement(nextDayButton));
            Thread.sleep(500);
            driver.findElement(nextDayButton).click();
            Thread.sleep(1500);
        }

        // check if the required day has available slots
        return !driver.findElement(By.xpath(String.format(availableRequiredDay, day, month, year))).getAttribute("class").contains("jgJhmZ");
    }

    private int calculateTimeSlotsDifference (int day,int month,int year){

        // Get the list of slots for required day
        List<WebElement> timeSlotsList = driver.findElements(By.xpath(String.format(timeSlots,day,month,year)));

        // Define the formatter for 12-hour format with AM/PM marker
        DateTimeFormatter twelveHourFormat = DateTimeFormatter.ofPattern("h:mm a");

        // Parse the strings into LocalTime objects
        LocalTime startTime = LocalTime.parse(timeSlotsList.get(0).getText(),twelveHourFormat);
        LocalTime endTime = LocalTime.parse(timeSlotsList.get(1).getText(),twelveHourFormat);

        // Calculate the difference
        Duration duration = Duration.between(startTime, endTime);
        return (int) duration.toMinutes();
    }


    private int checkCalendarType(int day, int month, int year){

        String slotsXpath = String.format(requiredDaySlotsType,day,month,year);

        if(driver.findElement(By.xpath(slotsXpath)).getAttribute("class").contains("kGOQXT")){
                return 1; //Indicate Single Time Frame
        } else if (driver.findElement(By.xpath(slotsXpath)).getAttribute("class").contains("dDSdzD")) {
                return 2; // Indicate Double Time Frames
        } else if (driver.findElement(By.xpath(slotsXpath)).getAttribute("class").contains("bXuRXP")) {
                return 3; // Indicate multiple time slots
        }
        return 0;
    }

    public boolean bookRequiredTimeSLot (int day, int month, int year, int hour, int minutes) throws InterruptedException {
        if (checkAvailabilityRequiredDay(day, month, year)) {
            switch (checkCalendarType(day, month, year)) {
                case 1:  // select the only visible time slot
                {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});",
                            driver.findElement(By.xpath(String.format(requiredDaySlotsType, day, month, year))));
                    Thread.sleep(1000);
                    driver.findElement(By.xpath(String.format(requiredDaySlotsType, day, month, year))).click();
                    return true;
                }
                case 2: // check the hour and select one of two intervals the best match
                {
                    String modifiedXpath = requiredDaySlotsType + "/span[%d]";
                    if (hour < 3) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});",
                                driver.findElement(By.xpath(String.format(modifiedXpath, day, month, year, 1))));
                        Thread.sleep(1000);
                        driver.findElement(By.xpath(String.format(modifiedXpath, day, month, year, 1))).click();
                    } else {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});",
                                driver.findElement(By.xpath(String.format(modifiedXpath, day, month, year, 2))));
                        Thread.sleep(1000);
                        driver.findElement(By.xpath(String.format(modifiedXpath, day, month, year, 2))).click();
                    }
                    return true;
                }
                case 3: // Get the difference in time slots and select the first availability if the required was not
                    // available
                {
                    int difference = calculateTimeSlotsDifference(day, month, year);

                    // Expand more button
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});",
                            driver.findElement(moreButton));

                    Thread.sleep(1500);
                    driver.findElement(moreButton).click();
                    // Wait until the DOM is updated to read all time slots in next step
                    Thread.sleep(1000);

                    do {
                        String modifiedXpath = String.format(requiredTimeSlot, day, month, year, hour, minutes);

                        // Check if the required time slot if exist or not
                        if (driver.findElement(By.xpath(String.format(requiredDaySlots, day, month, year))).getText().contains(String.format("%d:%02d", hour, minutes))) {
                           // Check if the required time slot available or already booked
                            if (!driver.findElement(By.xpath(modifiedXpath)).getAttribute("data-autotestid").contains("available-false")) {
                                for (int i = 0; i < 3; i++) {
                                    // Scroll to specific time slot
                                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView" +
                                                    "({block:'center'});",
                                            driver.findElement(By.xpath(modifiedXpath)));
                                    Thread.sleep(1500);
                                }
                                // Click Time sloe
                                driver.findElement(By.xpath(modifiedXpath)).click();
                                break; // Exit the loop if the condition is met
                            } else {
                                minutes += difference; // Increment the minutes
                                if (minutes >= 60) {
                                    if (hour < 12)
                                        hour++; // Increment the hour if minutes reach 60
                                    else hour = 1;
                                    minutes = minutes % 60; // Reset minutes
                                }
                            }
                        } else {
                            System.out.println(String.format("The required time slot %d:%02d on day %02d/%02d/%d is " +
                                            "not exist",hour,
                                    minutes,day,month,year));
                            return false;
                        }
                    } while (hour < 13); // Condition for the loop to continue
                    return true;
                }

            }
        }
        System.out.println(String.format("Required Day %02d/%02d/%d is not available", day, month, year));
        return false;
    }

    public boolean checkVisibilityOfCancelButton(){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(cancelBookingButton));

        return driver.findElement(cancelBookingButton).isEnabled();
    }


}
