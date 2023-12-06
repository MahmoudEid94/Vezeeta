package tests;

import Pages.SignUpPage;
import Utilities.Utilities;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class RegisterTests extends BaseTest{

    @Test
    public void testDuplicateUser_NEG(){
        SignUpPage signUp = homePage.clickSignUp();
        signUp.addUserName("Mahmoud");
        signUp.addPhone("01221604554");
        signUp.addEmail("mahmoudassemeid@gmail.com");
        signUp.selectGender('M');
        signUp.addBirthday(1,6,1994);
        signUp.addPassword("Tosca006");
        signUp.submitForm();
        Utilities.captureScreenshot(driver," Data");
        assertTrue(signUp.checkDuplicate().contains("already registered"),"Account Not Duplicate");
    }
}
