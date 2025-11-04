package com.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/java/resources/config.properties";
    
    static {
        loadProperties();
    }
    
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
    
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            logger.debug("Retrieved property: " + key + " = " + value);
        } else {
            logger.warn("Property not found: " + key);
        }
        return value;
    }
    
    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Retrieved property: " + key + " = " + value);
        return value;
    }
}