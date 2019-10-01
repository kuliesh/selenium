package net.ukr.mailauto.BaseClass;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class readCookies {

    static WebDriver driver;

    private static Cookie freemail;
    private static Cookie sid;
    private static Cookie as;

    String baseURLMail = "https://mail.ukr.net";
    String baseURLSessions = "https://mail.ukr.net/desktop#security/sessions";


    String login = "asdqwez";
    String pass = "++++++++++";



    @BeforeClass
    public static void setup () {
        WebDriverManager.chromedriver ().setup ();
        driver = new ChromeDriver();
    }

//    @AfterClass
//    public void stop()
//    {
//        driver.quit() ;
//    }

    @Test
    public void login() {
        driver.navigate().to(baseURLMail);

        driver.findElement(By.id ("id-l")).sendKeys(login);
        driver.findElement(By.id ("id-p")).sendKeys(pass);
        driver.findElement(By.cssSelector (".button")).click ();

        freemail = driver.manage().getCookieNamed("freemail");
        sid = driver.manage().getCookieNamed("sid");

        driver.navigate().to(baseURLSessions);
        as = driver.manage().getCookieNamed("as");

        //
        System.out.println("Cookie as " + driver.manage().getCookieNamed("freemail"));
        System.out.println("Cookie sid " +  driver.manage().getCookieNamed("sid"));

        driver.navigate ().to (baseURLMail);
        driver.navigate().refresh();
        driver.quit();

        driver = new ChromeDriver ();
        driver.navigate().to(baseURLMail);

        driver.manage().deleteAllCookies();

        driver.manage().addCookie(freemail);
        driver.manage().addCookie(sid);
        driver.manage().addCookie(as);

        //Перевірка чи зайшли на сторінку
        //Assert.assertEquals(driver.findElement(By.className(".login-button__user")).getText(), login+"@ukr.net");
    }
}
