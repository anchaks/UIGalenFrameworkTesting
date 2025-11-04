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

public class GalenLayoutTestMobile extends BaseTest
{
    @BeforeMethod
    public void setUpGalenTest() {
        String baseUrl = ConfigReader.getProperty("base.url");
        driver.get(baseUrl);
        extentTest.info("Navigated to: " + baseUrl + " for Galen layout testing");
    }

    @Test(priority = 3, description = "Verify login page layout on mobile")
    public void testLoginPageLayoutMobile() {
        extentTest.info("Starting mobile layout test");
        
        try {
            // Set mobile viewport
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));
            
            // Run Galen test
            LayoutReport layoutReport = Galen.checkLayout(driver, 
                "src/test/java/resources/galen_specs/login_page.gspec", 
                Arrays.asList("mobile"));
            
            // Create Galen test info
            GalenTestInfo test = GalenTestInfo.fromString("Login Page Mobile Layout");
            test.getReport().layout(layoutReport, "Check login page layout on mobile");
            galenTests.add(test);
            
            // Verify no layout errors
            if (layoutReport.errors() > 0) {
                logger.error("\n========================================");
                logger.error("MOBILE LAYOUT VALIDATION FAILED");
                logger.error("Total Errors: " + layoutReport.errors());
                logger.error("Total Warnings: " + layoutReport.warnings());
                logger.error("========================================");
                
                // Print detailed error information with actual values
                layoutReport.getValidationErrorResults().forEach(error -> {
                    logger.error("\n[ERROR] Spec: " + error.getSpec().toText());
                    if (error.getError() != null && error.getError().getMessages() != null) {
                        error.getError().getMessages().forEach(msg -> 
                            logger.error("  → " + msg)
                        );
                    }
                });
                logger.info("\n💡 TIP: Update your .gspec file with the actual values shown above");
                logger.error("========================================\n");
                
                // Write errors to file
                String errorFile = ErrorFileWriter.writeErrors(layoutReport, "Mobile");
                if (errorFile != null) {
                    logger.info("Errors written to: " + errorFile);
                    extentTest.info("Errors written to: " + errorFile);
                }
                
                extentTest.log(Status.FAIL, "Layout validation failed with " + layoutReport.errors() + " errors");
                Assert.fail("Layout validation failed with " + layoutReport.errors() + " errors");
            } else {
                logger.info("\n========================================");
                logger.info("✅ MOBILE LAYOUT VALIDATION PASSED");
                logger.info("No errors found - All values are matching!");
                logger.info("========================================\n");
                extentTest.log(Status.PASS, "Mobile layout validation passed");
            }
            
            logger.info("Mobile layout test completed successfully");
            
        } catch (IOException e) {
            extentTest.log(Status.FAIL, "Failed to run Galen layout test: " + e.getMessage());
            logger.error("Failed to run Galen layout test", e);
            Assert.fail("Failed to run Galen layout test: " + e.getMessage());
        }
    }

}
