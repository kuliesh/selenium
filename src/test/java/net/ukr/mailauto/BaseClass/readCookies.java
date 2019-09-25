package net.ukr.mailauto.BaseClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class readCookies {

    WebDriver driver;

    @Before
    public void start()
    {
        ChromeOptions opt = new ChromeOptions();
        driver = new ChromeDriver(opt);
    }

    @After
    public void stop()
    {
        driver.quit();
    }

    @Test
    public void writeCookies() {
        driver.get("https://www.ukr.net/ua/");
    }

}
