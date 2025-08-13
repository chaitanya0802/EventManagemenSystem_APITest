package utlities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getReporter() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/target/reports/ExtentReport.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setDocumentTitle("API Test Report");
            reporter.config().setReportName("RestAssured API Test Execution");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Chaitanya Malshikare");
            extent.setSystemInfo("Batch", "QEA 24-group1");
            extent.setSystemInfo("Tool", "RestAssured");
        }
        return extent;
    }
}

