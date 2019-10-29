package java.net.ukr.mailauto.Tests.appium;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import net.ukr.mailauto.Tests.listeners.EventsListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class TestForAndroidOnRealDevice {

    private static WebDriverWait wait;
    private static AndroidDriver<WebElement> driver;
    private static EventFiringWebDriver edr;

    private static String baseURL = "https://accounts.ukr.net/login";

    @BeforeClass
    public static void start() throws MalformedURLException {

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        dc.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.0");
        dc.setCapability("browserName", "Chrome");
        URL serverURL = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new AndroidDriver<>(serverURL, dc);
        edr = new EventFiringWebDriver (driver);
        edr.register(new EventsListener());

        wait = new WebDriverWait(edr, 5);
    }

    @AfterClass
    public static void stop() {
        driver.closeApp();
    }

    @Test
    public void androidTest() throws InterruptedException {

        //Массив url адреси сторінки для різних мов
        String[] termsURL = new String[3];
        termsURL[0] = "www.ukr.net/terms/";//укр
        termsURL[1] = "www.ukr.net/ru/terms/";//рус
        termsURL[2] = "www.ukr.net/terms/";//англ

        //Массив заголовків сторінки про конфеденційність для різних мов
        String[] titleTerms = new String[3];
        titleTerms[0] = "Угода про конфіденційність";
        titleTerms[1] = "Соглашение о конфиденциальности";
        titleTerms[2] = "Угода про конфіденційність";

        //Массив локаторів логотипу сторінки для кожної мови
        String[] logoPageTerms = new String[3];
        logoPageTerms[0] = "ua";
        logoPageTerms[1] = "ru";
        logoPageTerms[2] = "ua";

        driver.get(baseURL);
        edr.findElement(By.xpath("//a[@href][2]")).click();//создать ящик

        for (int i = 0; i <= 2; i++) {

            //Встановлюємо час очікування завантаження
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='header__lang']/button[" + (i + 1) + "]")));

            //Задаємо цикл зміни мови сторінки
            edr.findElement(By.xpath("//div[@class='header__lang']/button[" + (i + 1) + "]")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='//" + termsURL[i] + "']")));

            //перевіряємо що відображається сторінка Конфеденційності
            edr.findElement(By.xpath("//a[@href='//" + termsURL[i] + "']")).click();
            wait.until(numberOfWindowsToBe(2));

            ArrayList<String> tabs = new ArrayList<String>(edr.getWindowHandles());

            //Переходимо на нову вкладку браузера
            edr.switchTo().window(tabs.get(1));
            wait.until(visibilityOf(edr.findElement(By.cssSelector("h2"))));

            SoftAssert softAssertion = new SoftAssert();

            //Виконуємо перевірку заголовку до вибраної мови стоірнки
            String h2 = edr.findElement(By.cssSelector("h2")).getText();
            softAssertion.assertTrue(h2.contains(titleTerms[i]));

            //Перевірика валідності URL сторінки відповідно до вибраної мови
            String url = edr.getCurrentUrl();
            softAssertion.assertTrue(url.contains(termsURL[i]));

            //Пеервірка відповдного логотипу в залежності від мови сторінки
            List<WebElement> list = edr.findElements(By.xpath("//img[@src='img/terms-logoPageTerms-" + logoPageTerms[i] + ".gif']"));
            softAssertion.assertTrue(list.size() > 0, "logoPageTerms not found!");

            softAssertion.assertAll();

            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
    }
}