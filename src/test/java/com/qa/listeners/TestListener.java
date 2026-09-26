package com.qa.listeners;

import com.qa.base.DriverFactory;
import com.qa.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener monitors TestNG execution events.
 * Logs lifecycle events, captures failure screenshots, and attaches them to Allure reports.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("================================================================================");
        logger.info("STARTING TEST SUITE: {}", context.getName());
        logger.info("================================================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("================================================================================");
        logger.info("FINISHED TEST SUITE: {} | Total Passed: {}, Failed: {}, Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        logger.info("================================================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("STARTING TEST: {}.{}", result.getTestClass().getName(), result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.info("PASSED TEST: {}.{} (Duration: {} ms)",
                result.getTestClass().getName(), result.getMethod().getMethodName(), duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        
        logger.error("FAILED TEST: {}.{} (Duration: {} ms)", result.getTestClass().getName(), testName, duration);
        if (result.getThrowable() != null) {
            logger.error("FAILURE REASON: {}", result.getThrowable().getMessage());
        }

        try {
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null) {
                ScreenshotUtils.saveScreenshotToAllure(testName, driver);
                ScreenshotUtils.captureLocalScreenshot(driver, testName);
            } else {
                logger.warn("Driver was null during failure listener callback for test: {}", testName);
            }
        } catch (Exception e) {
            // Prevent screenshot failures from swallowing or overriding the original test failure
            logger.error("Error capturing failure artifacts for test: {}", testName, e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("SKIPPED TEST: {}.{}", result.getTestClass().getName(), result.getMethod().getMethodName());
    }
}
