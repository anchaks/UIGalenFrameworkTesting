package com.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Utility class for reading configuration properties from a properties file.
 * Loads configuration once during class initialization and provides static methods
 * to retrieve property values. Used to externalize test configuration like URLs,
 * browser settings, and environment details.
 */
public class ConfigReader {
    
    /** Logger instance for logging configuration operations */
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    
    /** Properties object holding all key-value pairs from the config file */
    private static Properties properties;
    
    /** Path to the configuration properties file */
    private static final String CONFIG_FILE_PATH = "src/test/java/resources/config.properties";
    
    // Static initializer block - runs once when class is first loaded
    static {
        loadProperties();
    }
    
    /**
     * Loads properties from the configuration file into memory
     * Called automatically when the class is first loaded
     * Throws RuntimeException if file cannot be loaded (fail-fast approach)
     */
    private static void loadProperties() {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }
    
    /**
     * Retrieves a property value by its key
     * 
     * @param key The property key to look up (e.g., "browser", "base.url")
     * @return The property value, or null if key doesn't exist
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            logger.debug("Retrieved property: " + key + " = " + value);
        } else {
            logger.warn("Property not found: " + key);
        }
        return value;
    }
    
    /**
     * Retrieves a property value by its key, with a default fallback value
     * 
     * @param key The property key to look up
     * @param defaultValue The value to return if the key is not found
     * @return The property value, or defaultValue if key doesn't exist
     */
    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Retrieved property: " + key + " = " + value);
        return value;
    }
}