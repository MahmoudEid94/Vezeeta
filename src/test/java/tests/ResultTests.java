package tests;

import Pages.AccountSearch;
import Pages.ResultPage;
import Utilities.Utilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ResultTests extends BaseTest{

    @Test (dataProvider = "Sorting Data", groups = {"Smoke","Regression"})
    public void testIncreasingSorting_POS(String speciality , String city, String order) throws InterruptedException {
        AccountSearch accountSearch = homePage.clickLogin().completeLogin();
        assertEquals(accountSearch.getAccountName(),"MAHMOUD","Login Failed");
        accountSearch.selectSpeciality(speciality);
        accountSearch.selectCity(city);
        ResultPage resultPage = accountSearch.clickSearchButton();
        switch (order){
            case "Inc":{
                resultPage.sortPricesIncreasing();
                assertTrue(resultPage.isIncreasing());
                assertTrue(resultPage.loopOnPages(1));
                break;
            }
            case "Dec":{
                resultPage.sortPricesDecreasing();
                assertTrue(resultPage.isDecreasing());
                assertTrue(resultPage.loopOnPages(2));
                break;
            }
            default: System.out.println("No specified order");
        }

    }

    @DataProvider (name = "Sorting Data")
    public Object[][] readSortDataFromExcel(){
        return Utilities.readExcel("Resources/TestData/SortingData.xlsx",0);
    }


    @Test (groups = "Regression")
    public void testOpenRandomDrPage() throws InterruptedException {
        AccountSearch accountSearch =homePage.clickLogin().completeLogin();
        assertEquals(accountSearch.getAccountName(),"MAHMOUD","Login Failed");
        accountSearch.selectSpeciality("Dermatology (Skin)");
        ResultPage resultPage = accountSearch.clickSearchButton();
        resultPage.choseRandomDr();
    }

}
