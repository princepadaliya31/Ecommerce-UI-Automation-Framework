package com.qa.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for capturing local file screenshots and attaching PNG byte arrays to Allure reports.
 */
public class ScreenshotUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);

    @Attachment(value = "Failure Screenshot - {0}", type = "image/png")
    public static byte[] saveScreenshotToAllure(String testName, WebDriver driver) {
        if (driver == null) {
            logger.warn("WebDriver instance is null; skipping Allure screenshot attachment for test: {}", testName);
            return new byte[0];
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture Allure screenshot attachment for test: {}", testName, e);
            return new byte[0];
        }
    }

    public static String captureLocalScreenshot(WebDriver driver, String rawScreenshotName) {
        if (driver == null) {
            logger.warn("WebDriver instance is null; skipping local screenshot capture.");
            return "";
        }
        try {
            // Sanitize filename to remove invalid path characters
            String sanitizedName = rawScreenshotName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = sanitizedName + "_" + timestamp + ".png";

            Path screenshotDir = Paths.get("target", "screenshots");
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path targetPath = screenshotDir.resolve(fileName);
            Files.copy(source.toPath(), targetPath);
            
            logger.info("Saved failure screenshot to: {}", targetPath.toAbsolutePath());
            return targetPath.toAbsolutePath().toString();
        } catch (Exception e) {
            logger.error("Failed to save local failure screenshot", e);
            return "";
        }
    }
}
