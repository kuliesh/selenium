package net.ukr.mailauto.Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Drivers {


    private static String arguments;

    public enum Browser {
        CHROME,
        FIREFOX,
        EDGE
    }


    public static WebDriver getDriver (Browser browser, String arguments) {
        WebDriver driver;

        {

            switch (browser) {
                case CHROME:
                    WebDriverManager.chromedriver ().setup ();
                    ChromeOptions options = new ChromeOptions ();
                    options.addArguments ("start-fullscreen");
                    WebDriver driverChrome = new ChromeDriver (options);
                    return driverChrome;

                case FIREFOX:
                    WebDriverManager.firefoxdriver ().setup ();
                    WebDriver driverFirefox = new FirefoxDriver ();
                    driverFirefox.manage ().window ().maximize ();
                    return driverFirefox;


                case EDGE:
                    WebDriverManager.edgedriver ().setup ();
                    WebDriver driverEdge = new EdgeDriver ();
                    return driverEdge;


            }
            ;
        }
        return null;
    }
}