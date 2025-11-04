package testClasses;

import java.io.IOException;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.utils.ConfigReader;
import com.utils.ErrorFileWriter;

import base.BaseTest;

/**
 * GalenLayoutTestMobile - Test class for validating page layout on mobile viewports.
 * Uses Galen Framework to verify UI elements are properly positioned and sized
 * for mobile devices according to specifications in .gspec files.
 * Tests responsive design for mobile screen sizes (375x667 - iPhone size).
 */
public class GalenLayoutTestMobile extends BaseTest
{
    /**
     * Setup method executed before each test in this class
     * Navigates to the base URL to prepare for layout testing
     */
    @BeforeMethod
    public void setUpGalenTest() {
        String baseUrl = ConfigReader.getProperty("base.url");
        driver.get(baseUrl);
        extentTest.info("Navigated to: " + baseUrl + " for Galen layout testing");
    }

    /**
     * Tests the login page layout on mobile viewport (375x667)
     * Validates element positioning, sizing, alignment, and spacing
     * according to the specifications in login_page.gspec file
     * 
     * Priority 3 - Runs third in the test suite (after desktop and tablet)
     */
    @Test(priority = 3, description = "Verify login page layout on mobile")
    public void testLoginPageLayoutMobile() {
        extentTest.info("Starting mobile layout test");
        
        try {
            // Set mobile viewport dimensions (iPhone 6/7/8 size)
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));
            
            // Run Galen layout validation with "mobile" tag from .gspec file
            LayoutReport layoutReport = Galen.checkLayout(driver, 
                "src/test/java/resources/galen_specs/login_page.gspec", 
                Arrays.asList("mobile")); // Uses mobile-specific specs
            
            // Create Galen test info for HTML report generation
            GalenTestInfo test = GalenTestInfo.fromString("Login Page Mobile Layout");
            test.getReport().layout(layoutReport, "Check login page layout on mobile");
            galenTests.add(test); // Add to shared list for final report
            
            // Verify no layout errors - fail test if any errors found
            if (layoutReport.errors() > 0) {
                // Log detailed error information to console
                logger.error("\n========================================");
                logger.error("MOBILE LAYOUT VALIDATION FAILED");
                logger.error("Total Errors: " + layoutReport.errors());
                logger.error("Total Warnings: " + layoutReport.warnings());
                logger.error("========================================");
                
                // Print detailed error information with actual values
                layoutReport.getValidationErrorResults().forEach(error -> {
                    logger.error("\n[ERROR] Spec: " + error.getSpec().toText());
                    // Show actual vs expected values to help fix the specs
                    if (error.getError() != null && error.getError().getMessages() != null) {
                        error.getError().getMessages().forEach(msg -> 
                            logger.error("  → " + msg)
                        );
                    }
                });
                logger.info("\n💡 TIP: Update your .gspec file with the actual values shown above");
                logger.error("========================================\n");
                
                // Write errors to timestamped file for reference
                String errorFile = ErrorFileWriter.writeErrors(layoutReport, "Mobile");
                if (errorFile != null) {
                    logger.info("Errors written to: " + errorFile);
                    extentTest.info("Errors written to: " + errorFile);
                }
                
                // Log failure in ExtentReports and fail the test
                extentTest.log(Status.FAIL, "Layout validation failed with " + layoutReport.errors() + " errors");
                Assert.fail("Layout validation failed with " + layoutReport.errors() + " errors");
            } else {
                // All layout checks passed
                logger.info("\n========================================");
                logger.info("✅ MOBILE LAYOUT VALIDATION PASSED");
                logger.info("No errors found - All values are matching!");
                logger.info("========================================\n");
                extentTest.log(Status.PASS, "Mobile layout validation passed");
            }
            
            logger.info("Mobile layout test completed successfully");
            
        } catch (IOException e) {
            // Handle Galen framework errors
            extentTest.log(Status.FAIL, "Failed to run Galen layout test: " + e.getMessage());
            logger.error("Failed to run Galen layout test", e);
            Assert.fail("Failed to run Galen layout test: " + e.getMessage());
        }
    }

}
