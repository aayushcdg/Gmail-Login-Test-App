package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.opencsv.CSVWriter;

public class GmailLoginTest {
    private static CSVWriter csvWriter;
    private static FirefoxDriver driver;

    public static void main(String[] args) throws InterruptedException, IOException {
        SimpleHttpServer.startServer();

        System.setProperty("webdriver.gecko.driver", "/Users/ajain03/Desktop/TPJ Project Submission/SourceCode_Jain_Dua/geckodriver");
        driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin");

        // Add a delay to ensure the page is fully loaded
        Thread.sleep(2000);

        driver.findElement(By.id("identifierId")).sendKeys("aayush");
        driver.findElement(By.xpath("//span[text()='Next']")).click(); // Changed to use the 'Next' button

        String expected = "Couldn't find your Google Account";

        try {
            // Wait for the error message element to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='o6cuMc']")));
        } catch (Exception e) {
            System.out.println("Element not found: " + e.getMessage());
        }

        String actual;
        try {
            actual = driver.findElement(By.xpath("//div[@class='o6cuMc']")).getText();
            System.out.println(actual);
        } catch (Exception e) {
            actual = "";
            System.out.println("Error getting element text: " + e.getMessage());
        }

        // Check if login is successful and call saveToCSV accordingly
        if (expected.equals(actual)) {
            System.out.println("fail");
            saveToCSV("aayush", "fail");
        } else {
            System.out.println("pass");
            saveToCSV("aayush", "pass");
        }

        // Close the browser and export the CSV file
        closeResources();
    }

    private static void saveToCSV(String username, String status) throws IOException {
        String csvFile = "/Users/ajain03/Desktop/TPJ Project Submission/SourceCode_Jain_Dua/Test/GmailLoginTest/login_results.csv";
        if (csvWriter == null) {
            FileWriter writer = new FileWriter(csvFile, true);
            csvWriter = new CSVWriter(writer);
            File file = new File(csvFile);
            if (file.exists() && file.length() == 0) {
                csvWriter.writeNext(new String[]{"Username", "Attempt pass/fail", "Login Attempt", "IP address"});
            }
        }
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String[] data = {username, status.toUpperCase(), formattedDateTime, ipAddress};
        csvWriter.writeNext(data);
        csvWriter.flush();
    }

    private static void closeResources() {
        if (driver != null) {
            driver.quit();
        }
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch (IOException e) {
                System.out.println("Error closing CSV writer: " + e.getMessage());
            }
        }
    }
}
