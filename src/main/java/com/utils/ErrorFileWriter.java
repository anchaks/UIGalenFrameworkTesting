package com.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.galenframework.reports.model.LayoutReport;

/**
 * ErrorFileWriter - Utility class for writing Galen Framework layout validation errors to text files.
 * Creates detailed, timestamped error reports when layout tests fail, making it easier
 * to identify and fix layout issues. Provides cleanup methods to manage old error files.
 */
public class ErrorFileWriter {
    
    /** Directory where error files will be saved */
    private static final String ERROR_OUTPUT_DIR = "test-output/layout-errors";
    
    /** Date format for timestamps in filenames and content */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Writes layout validation errors to a timestamped text file
     * Creates a formatted error report with error counts, detailed messages,
     * and helpful tips for fixing the issues
     * 
     * @param layoutReport The Galen layout report containing errors and warnings
     * @param testType The type of test (e.g., "Desktop", "Tablet", "Mobile") - used in filename
     * @return The path to the created error file, or null if no errors were found
     */
    public static String writeErrors(LayoutReport layoutReport, String testType) {
        // Don't create file if there are no errors
        if (layoutReport == null || layoutReport.errors() == 0) {
            return null;
        }
        
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(ERROR_OUTPUT_DIR));
            
            // Generate timestamped filename for unique identification
            String timestamp = dateFormat.format(new Date());
            String fileName = String.format("%s/GalenLayoutErrors_%s_%s.txt", 
                                          ERROR_OUTPUT_DIR, testType, timestamp);
            
            // Build error content with formatting
            StringBuilder errorContent = new StringBuilder();
            errorContent.append("========================================\n");
            errorContent.append(testType.toUpperCase()).append(" LAYOUT VALIDATION FAILED\n");
            errorContent.append("Test Execution Time: ").append(timestamp.replace("_", " ")).append("\n");
            errorContent.append("Total Errors: ").append(layoutReport.errors()).append("\n");
            errorContent.append("Total Warnings: ").append(layoutReport.warnings()).append("\n");
            errorContent.append("========================================\n\n");
            
            // Add detailed error information for each failed validation
            layoutReport.getValidationErrorResults().forEach(error -> {
                errorContent.append("[ERROR] Spec: ").append(error.getSpec().toText()).append("\n");
                // Include error messages with actual vs expected values
                if (error.getError() != null && error.getError().getMessages() != null) {
                    error.getError().getMessages().forEach(msg -> 
                        errorContent.append("  → ").append(msg).append("\n")
                    );
                }
                errorContent.append("\n");
            });
            
            // Add helpful tip for fixing errors
            errorContent.append("========================================\n");
            errorContent.append("💡 TIP: Update your .gspec file with the actual values shown above\n");
            errorContent.append("========================================\n");
            
            // Write formatted content to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.write(errorContent.toString());
            }
            
            return fileName;
            
        } catch (IOException e) {
            System.err.println("Failed to write error file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Cleans up old error files to prevent accumulation
     * Keeps only the most recent N files and deletes older ones
     * Optional maintenance method to manage disk space
     * 
     * @param keepLastN Number of recent error files to keep (older files will be deleted)
     */
    public static void cleanupOldErrorFiles(int keepLastN) {
        try {
            File dir = new File(ERROR_OUTPUT_DIR);
            if (!dir.exists()) return;
            
            // Get all text files in the error directory
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files == null || files.length <= keepLastN) return;
            
            // Sort by last modified date (newest first)
            java.util.Arrays.sort(files, (f1, f2) -> 
                Long.compare(f2.lastModified(), f1.lastModified()));
            
            // Delete older files beyond the keepLastN limit
            for (int i = keepLastN; i < files.length; i++) {
                files[i].delete();
            }
            
        } catch (Exception e) {
            System.err.println("Failed to cleanup old error files: " + e.getMessage());
        }
    }
}
