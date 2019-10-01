package net.ukr.mailauto.BaseClass;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class readCookies extends Drivers {

    String baseURLMail = "https://mail.ukr.net";
    String baseURLSessions = "https://mail.ukr.net/desktop#security/sessions";
    WebDriver driver;


    String login = "asdqwez";
    String pass = "++++++++++";

    private static Cookie freemail;
    private static Cookie sid;
    private static Cookie as;

    @Before
    public void start()
    {
        ChromeOptions opt = new ChromeOptions();
        driver = new ChromeDriver(opt);
    }

    @After
    public void stop()
    {
        driver.quit() ;
    }

    @Test
    public void writeCookies() {
        //Перехід на сторінку логіна та виконання входу до поштового акаунту
        driver.navigate().to(baseURLMail);
        driver.findElement(By.cssSelector("#id-l")).sendKeys(login);
        driver.findElement(By.cssSelector("#id-p")).sendKeys(pass);
        driver.findElement(By.cssSelector(".button")).click();

        //Записуємор кукі
        freemail = driver.manage().getCookieNamed("freemail");
        sid = driver.manage().getCookieNamed("sid");

        //Перехід на сторінку Налаштувань для отримання кук
        driver.navigate().to(baseURLSessions);
        //Записуємор кукі
        as = driver.manage().getCookieNamed("as");

        System.out.println("Cookies for auth: " + driver.manage().getCookies());

        //Видалення кук з браузреа
        driver.manage().deleteAllCookies();

        //Вихід з поштової скриньки
        driver.quit ();
    }

    @Test
    public void readCookies(){

        //Виконуємо вхід по кукам
        driver.navigate().to(baseURLMail);

        //передача браузеру необхідних кук
        driver.manage().addCookie(freemail);
        driver.manage().addCookie(sid);
        driver.manage().addCookie(as);

        //Перевірка чи зайшли на сторінку
        Assert.assertEquals(driver.findElement(By.className(".login-button__user")).getText(), login+"@ukr.net");
    }
}
