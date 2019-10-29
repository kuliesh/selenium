package net.ukr.mailauto.Tests.home;

import io.github.bonigarcia.wdm.WebDriverManager;

import net.ukr.mailauto.Tests.dataFormTermsOfService.DataForTermOfServiceUA;
import net.ukr.mailauto.Tests.listeners.EventsListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.Set;
public class TestTermOfService extends EventsListener {

    static WebDriver driver;
    private static EventFiringWebDriver edr;
    private static WebDriverWait wait;

    private static String baseUrl = "https://accounts.ukr.net/registration";
    private static String ukrmailUrl = "https://mail.ukr.net";

    private static String verLang = "h1.header-title";

    private static int i = 10;

    public static String parentHandle = "parentHandle";

    @BeforeClass
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        edr = new EventFiringWebDriver (driver);
        edr.register(new EventsListener());

        wait = new WebDriverWait(edr, 5);

        driver.navigate().to(baseUrl);
        parentHandle = driver.getWindowHandle(); // get the registration page handle
    }

    @AfterClass
    public static void cleanup() {
        driver.quit();
    }

    public ExpectedCondition<String> anyWindowOtherThan(Set<String> windows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver input) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(windows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }

    //Тест для перевірки, що по кліку на "угоду про використання" відкривається угода про використання для української локалізації
    @Test
    public void testOpenTermOfServiceUA() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        DataForTermOfServiceUA dataForTermOfServiceUA = new DataForTermOfServiceUA();

        edr.findElement(By.cssSelector(UA)).click();
        //Перевіряємо що мова Українська

        Assert.assertEquals(actualTextForTermsOfService, edr.findElement(By.cssSelector(verLang)).getText(),
                messageErrorTextForTermsOfService);
        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector(".confirm-terms [data-tooltip]")).click();

        //перемикаємо фокус WebDriver на нщойно відкрити вкладку
        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrmailUrl + urlForTermsOfService, driver.getCurrentUrl(),
                messageErrorUrlForTermsOfService);

        softAssertion.assertEquals(actualHeaderForTermsOfService, edr.findElement(By.cssSelector(".register > h3")).getText(),
                messageErrorHeaderForTermsOfService);

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }
}
