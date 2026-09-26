package com.qa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read application configuration.
 * System properties pass via command line (-Dkey=value) override config.properties entries.
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.warn("config.properties not found in classpath. Using default property values.");
            } else {
                properties.load(input);
            }
        } catch (Exception e) {
            logger.error("Failed to load config.properties file", e);
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }
        return properties.getProperty(key, defaultValue);
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static String getBaseUrl() {
        return getProperty("baseUrl", "https://www.saucedemo.com/");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicitWait", "10"));
    }

    public static boolean isGridEnabled() {
        return Boolean.parseBoolean(getProperty("grid.enabled", "false"));
    }

    public static String getGridUrl() {
        return getProperty("grid.url", "http://localhost:4444/");
    }
}
