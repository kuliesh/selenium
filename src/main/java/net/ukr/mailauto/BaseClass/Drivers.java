package net.ukr.mailauto.BaseClass;

import net.ukr.mailauto.BaseClass.utils.NoSuchBrowserException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

public class Drivers {

    public static WebDriver getDriver(Browsers browser, String argument) throws NoSuchBrowserException {

        switch (browser)
        {
            case CHROME:
                return new ChromeDriver (new ChromeOptions().addArguments(argument));
            case FIREFOX:
                return new FirefoxDriver(new FirefoxOptions().addArguments(argument));
            case IE:
                return new InternetExplorerDriver(new InternetExplorerOptions().addCommandSwitches(argument));
            default:
                throw new NoSuchBrowserException(browser.toString() + " is not supported yet");
        }
    }
}