package com.qa.tests;

import com.qa.base.BaseTest;
import com.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameworkVerificationTest extends BaseTest {

    @Test(description = "Verify framework driver initialization, navigation, and page reachability")
    public void testFrameworkInitialization() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "SauceDemo login logo should be visible on initial load.");
        Assert.assertEquals(getDriver().getTitle(), "Swag Labs", "Page title should be 'Swag Labs'.");
    }
}
