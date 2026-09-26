package com.qa.base;

import com.qa.utils.ConfigReader;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

/**
 * Thread-safe DriverFactory providing WebDriver management using ThreadLocal.
 * Supports local execution via native Selenium 4 Selenium Manager and optional RemoteWebDriver execution via Selenium Grid.
 */
public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    public static WebDriver initializeDriver() {
        return initializeDriver(ConfigReader.getBrowser());
    }

    public static WebDriver initializeDriver(String browser) {
        String targetBrowser = (browser != null && !browser.trim().isEmpty()) ? browser : ConfigReader.getBrowser();
        boolean isHeadless = ConfigReader.isHeadless();
        boolean isGrid = ConfigReader.isGridEnabled();
        String gridUrl = ConfigReader.getGridUrl();

        logger.info("Initializing WebDriver session | Browser: {} | Headless: {} | Grid Enabled: {}",
                targetBrowser, isHeadless, isGrid);

        try {
            WebDriver driver;

            switch (targetBrowser.toLowerCase()) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) {
                        firefoxOptions.addArguments("-headless");
                    }
                    firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    
                    if (isGrid) {
                        logger.info("Connecting to Remote Selenium Grid at: {}", gridUrl);
                        URL remoteUrl = URI.create(gridUrl).toURL();
                        driver = new RemoteWebDriver(remoteUrl, firefoxOptions);
                    } else {
                        driver = new FirefoxDriver(firefoxOptions);
                    }
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (isHeadless) {
                        edgeOptions.addArguments("--headless=new");
                    }
                    edgeOptions.addArguments("--disable-gpu", "--no-sandbox", "--remote-allow-origins=*");
                    edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    
                    if (isGrid) {
                        logger.info("Connecting to Remote Selenium Grid at: {}", gridUrl);
                        URL remoteUrl = URI.create(gridUrl).toURL();
                        driver = new RemoteWebDriver(remoteUrl, edgeOptions);
                    } else {
                        driver = new EdgeDriver(edgeOptions);
                    }
                    break;

                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isHeadless) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    chromeOptions.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*", "--window-size=1920,1080");
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    
                    if (isGrid) {
                        logger.info("Connecting to Remote Selenium Grid at: {}", gridUrl);
                        URL remoteUrl = URI.create(gridUrl).toURL();
                        driver = new RemoteWebDriver(remoteUrl, chromeOptions);
                    } else {
                        driver = new ChromeDriver(chromeOptions);
                    }
                    break;
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // Explicit waits enforced
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

            threadLocalDriver.set(driver);
            logger.info("WebDriver session initialized successfully for thread ID: {}", Thread.currentThread().getId());
            return getDriver();

        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: {}", targetBrowser, e);
            quitDriver();
            throw new RuntimeException("Could not initialize WebDriver for browser: " + targetBrowser, e);
        }
    }

    // Alias for backward compatibility
    public static WebDriver initDriver(String browser) {
        return initializeDriver(browser);
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            try {
                logger.info("Cleaning up WebDriver session for thread ID: {}", Thread.currentThread().getId());
                driver.quit();
            } catch (Exception e) {
                logger.error("Error encountered while quitting WebDriver", e);
            } finally {
                threadLocalDriver.remove();
                logger.info("ThreadLocal driver instance removed successfully.");
            }
        }
    }
}
