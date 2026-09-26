package com.qa.listeners;

import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Controlled TestNG Retry Analyzer.
 * 
 * Rules:
 * 1. Maximum retry count = 1.
 * 2. Only retries transient infrastructure/WebDriver exceptions (e.g., StaleElementReferenceException, NoSuchSessionException).
 * 3. NEVER retries java.lang.AssertionError to prevent defect suppression or hiding functional bugs.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1;

    @Override
    public boolean retry(ITestResult result) {
        Throwable throwable = result.getThrowable();

        // Rule: Never retry assertion failures (functional defects)
        if (throwable instanceof AssertionError) {
            logger.info("[RETRY ANALYZER] Test '{}' failed due to AssertionError. Retries disabled for assertion failures.",
                    result.getMethod().getMethodName());
            return false;
        }

        // Rule: Retry only transient WebDriver / infrastructure exceptions up to MAX_RETRY_COUNT
        if (isTransientWebDriverException(throwable) && retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("[RETRY ANALYZER] Retrying transient WebDriver failure for test '{}' (Attempt {}/{}) | Exception: {}",
                    result.getMethod().getMethodName(), retryCount, MAX_RETRY_COUNT, throwable.getClass().getSimpleName());
            return true;
        }

        return false;
    }

    private boolean isTransientWebDriverException(Throwable throwable) {
        if (throwable == null) return false;
        return throwable instanceof StaleElementReferenceException
                || throwable instanceof NoSuchSessionException
                || (throwable instanceof WebDriverException && !(throwable instanceof AssertionError));
    }
}
