package com.qa.dataproviders;

import com.qa.utils.TestDataUtils;
import org.testng.annotations.DataProvider;

/**
 * DataProvider class containing test data sources for login test scenarios.
 * Protects passwords and supports both CSV-driven and inline TestNG providers.
 */
public class LoginDataProvider {

    @DataProvider(name = "loginCsvData")
    public static Object[][] getLoginCsvData() {
        return TestDataUtils.getCSVData("testdata/login-data.csv");
    }

    @DataProvider(name = "invalidCredentialsData")
    public static Object[][] getInvalidCredentialsData() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."},
                {"invalid_user", "secret_sauce", "Epic sadface: Username and password do not match any user in this service"},
                {"standard_user", "wrong_password", "Epic sadface: Username and password do not match any user in this service"},
                {"", "secret_sauce", "Epic sadface: Username is required"},
                {"standard_user", "", "Epic sadface: Password is required"}
        };
    }
}
