package net.ukr.mailauto.BaseClass;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.edge.EdgeDriver;

public class Drivers {

    public static WebDriver getDriver(Browsers browser, String argument) throws NoSuchBrowserException {
        WebDriver driver;

        {

            switch (browser) {
                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("start-fullscreen");
                    WebDriver driverChrome = new ChromeDriver(options);
                    return driverChrome;

                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    WebDriver driverFirefox = new FirefoxDriver();
                    driverFirefox.manage().window().maximize();
                    return driverFirefox;

                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    WebDriver driverEdge = new EdgeDriver();
                    return driverEdge;

                default:
                    throw new NoSuchBrowserException(browser.toString() + " is not supported yet");
            }
        }
    }
}