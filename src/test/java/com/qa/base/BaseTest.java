package com.qa.base;

import com.qa.utils.ConfigReader;
import com.qa.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * BaseTest manages test method lifecycle and driver creation/cleanup.
 * Ensures each test receives an independent, isolated browser session.
 */
public class BaseTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String browser) {
        String activeBrowser = (browser != null && !browser.isEmpty()) ? browser : ConfigReader.getBrowser();
        logger.info("Initializing test setup | Target Browser: {}", activeBrowser);

        WebDriver driver = DriverFactory.initializeDriver(activeBrowser);
        String baseUrl = ConfigReader.getBaseUrl();
        
        logger.info("Navigating to application base URL: {}", baseUrl);
        driver.get(baseUrl);

        // Verify initial page reachability
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Initial page navigation failed; current URL is null.");
        logger.info("Successfully navigated to SauceDemo. Current URL: {}", currentUrl);
    }

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(org.testng.ITestResult result) {
        if (result != null && result.getStatus() == org.testng.ITestResult.FAILURE) {
            String testName = result.getMethod().getMethodName();
            logger.error("FAILED TEST DETECTED: {}.{} | Capturing failure screenshots...",
                    result.getTestClass().getName(), testName);
            try {
                WebDriver driver = getDriver();
                if (driver != null) {
                    ScreenshotUtils.saveScreenshotToAllure(testName, driver);
                    ScreenshotUtils.captureLocalScreenshot(driver, testName);
                }
            } catch (Exception e) {
                logger.error("Error capturing failure screenshot for test: {}", testName, e);
            }
        }
        logger.info("Executing test teardown and driver cleanup.");
        DriverFactory.quitDriver();
    }
}
