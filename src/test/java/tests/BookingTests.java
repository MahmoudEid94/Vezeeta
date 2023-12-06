package tests;

import Pages.AccountSearch;
import Pages.DrPage;
import Pages.HomePage;
import Pages.ResultPage;
import Utilities.Utilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BookingTests extends BaseTest{


    @Test (dataProvider= "BookingSlotsData")
    public void testSuccessfulSlotBooking (String drName, String day, String month, String year, String hour, String minutes) throws InterruptedException {


        int dayInt = Integer.parseInt(day.replace(".0",""));
        int monthInt = Integer.parseInt(month.replace(".0",""));
        int yearInt = Integer.parseInt(year.replace(".0",""));
        int hourInt = Integer.parseInt(hour.replace(".0",""));
        int minuteInt = Integer.parseInt(minutes.replace(".0",""));

        AccountSearch accountSearch = homePage.clickLogin().completeLogin();
        assertEquals(accountSearch.getAccountName(),"MAHMOUD","Login Failed");
        accountSearch.searchByName(drName);
        ResultPage resultPage = accountSearch.clickSearchButton();
        DrPage drPage = resultPage.openSpecificDr(drName);
        if (drPage.bookRequiredTimeSLot(dayInt,monthInt,yearInt,hourInt,minuteInt)){
            assertTrue(drPage.checkVisibilityOfCancelButton(),"Booking Not Completed: neither Day is available nor Time slot is exist");
        }

    }

    @DataProvider(name="BookingSlotsData")
    public Object[][] readLoginDataFromExcel(){
        return Utilities.readExcel("Resources/TestData/BookingSlots.xlsx",0);
    }
}
