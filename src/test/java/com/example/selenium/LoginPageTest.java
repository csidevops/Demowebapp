import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HeadlessSeleniumTest {
    public static void main(String[] args) {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\Users\csidevops\ChromeDriver\chromedriver-win64");

        // Configure ChromeOptions for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver(options);

        // Open the URL
        driver.get("http://localhost:8080/Demowebapp"); 

        // Check if the page title contains a keyword
        String pageTitle = driver.getTitle();
        if (pageTitle.contains("Demo Web App")) { 
            System.out.println("Test passed: Web portal appeared successfully.");
        } else {
            System.out.println("Test failed: Web portal did not appear as expected.");
        }

        // Close the browser
        driver.quit();
    }
}
