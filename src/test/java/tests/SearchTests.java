package tests;

import Pages.AccountSearch;
import Pages.ResultPage;
import Utilities.Utilities;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SearchTests extends BaseTest{

    @Test (dataProvider = "SearchData",groups = {"Smoke","Regression"})
    public void testSearchByDifferentCriteria (String speciality, String city, String area, String drName) throws InterruptedException {
        AccountSearch accountSearch =  homePage.clickLogin().completeLogin();
        assertEquals(accountSearch.getAccountName(),"MAHMOUD","Login Failed");
        if (speciality!= null) {
            accountSearch.selectSpeciality(speciality);
            assertEquals(accountSearch.checkSelectedSpeciality(),speciality,"Wrong Speciality");
        }
        if(city!= null){
            accountSearch.selectCity(city);
            assertEquals(accountSearch.checkSelectedCity(),city,"Wrong City");
        }
        if (area!=null) accountSearch.selectArea();
        if (drName!=null){
            accountSearch.searchByName(drName);
        }
        ResultPage result = accountSearch.clickSearchButton();
        Assert.assertNotEquals(result.checkCountsOfResults(),0,"Wrong Numbers");
    }

    @DataProvider(name="SearchData")
    public Object[][] readSearchDataFromExcel(){
        return Utilities.readExcel("Resources/TestData/SearchFields.xlsx",0);
    }

    @Test (dataProvider = "SearchData_NEG",groups = {"Smoke","Regression"})
    public void testSearchByDifferentCriteria_Neg (String speciality, String city, String area, String drName) throws InterruptedException {
        AccountSearch accountSearch =  homePage.clickLogin().completeLogin();
        assertEquals(accountSearch.getAccountName(),"MAHMOUD","Login Failed");
        if (speciality!= null) {
            accountSearch.selectSpeciality(speciality);
            assertEquals(accountSearch.checkSelectedSpeciality(),speciality,"Wrong Speciality");
        }
        if(city!= null){
            accountSearch.selectCity(city);
            assertEquals(accountSearch.checkSelectedCity(),city,"Wrong City");
        }
        if (area!=null) accountSearch.selectArea();
        if (drName!=null){
            accountSearch.searchByName(drName);
        }
        ResultPage result = accountSearch.clickSearchButton();
        Assert.assertEquals(result.checkCountsOfResults(),0,"Wrong Numbers");
        Assert.assertEquals(result.checkCountsOfDrCards(),0,"Wrong Numbers, There are Dr Cards");
    }

    @DataProvider(name="SearchData_NEG")
    public Object[][] readSearchDataFromExcel_NEG(){
        return Utilities.readExcel("Resources/TestData/SearchFields.xlsx",1);
    }


}
