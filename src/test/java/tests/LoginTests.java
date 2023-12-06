package tests;

import Pages.LoginPage;
import Pages.AccountSearch;
import Utilities.Utilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LoginTests extends BaseTest{

    @Test(dataProvider = "LoginTestData_P01", groups = {"Smoke","Regression"})
    public void testSuccessfulLogin(String userName , String password, String testType){
        LoginPage loginPage = homePage.clickLogin();
        loginPage.addUserName(userName);
        loginPage.addPassword(password);
        AccountSearch accountSearch =  loginPage.clickLoginButton();
        switch (testType){
            case "P":
            case "p":
            {
                assertEquals(accountSearch.getAccountName(),"MAHMOUD","Login Failed");
                break;
            }
            case "N":
            case "n":
            {
                assertTrue(loginPage.verifyWrongLogin(),"Login Passed");
                break;
            }
            default:
            {
                fail("Wrong Data");
            }
        }

        Utilities.captureScreenshot(driver, "Login");
    }

    @DataProvider(name="LoginTestData_P01")
    public Object[][] readLoginDataFromExcel_P01(){
        return Utilities.readExcel("Resources/TestData/LoginTestData.xlsx",0);
    }

    @Test(dataProvider = "LoginTestData_P02", groups = "Regression")
    public void testInvalidLogin(String userName , String password, String testType){
        LoginPage loginPage = homePage.clickLogin();
        loginPage.addUserName(userName);
        if (password == null){
            loginPage.addPassword("");
        } else {
            loginPage.addPassword(password);
        }
        loginPage.clickLoginButton();
        switch (testType){
            case "EmptyPass":
            {
                assertTrue(loginPage.verifyEmptyPassword(),"Login Passed");
                break;
            }
            case "InvalidUser":
            {
                assertTrue(loginPage.verifyInvalidMobile(),"Login Passed");
                break;
            }
            default:
            {
                fail("Wrong Data");
            }
        }

        Utilities.captureScreenshot(driver, "Login");
    }

    @DataProvider(name="LoginTestData_P02")
    public Object[][] readLoginDataFromExcel_P02(){
        return Utilities.readExcel("Resources/TestData/LoginTestData.xlsx",1);
    }
}
