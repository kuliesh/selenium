package java.net.ukr.mailauto.Tests.appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.ukr.mailauto.Tests.listeners.EventsListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.support.events.EventFiringWebDriver;

public class TestForAndroidOnEmulator{

    private static WebDriverWait wait;
    private static AndroidDriver<WebElement> driver;
    private static EventFiringWebDriver edr;

    @BeforeClass
    public static void start() throws MalformedURLException {

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.0");
        dc.setCapability("appPackage", "com.android.calculator2");
        dc.setCapability("appActivity", "com.android.calculator2.Calculator");

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
    public void androidTest() {

        edr.findElement(By.id("com.android.calculator2:id/digit_1")).click();
        edr.findElement(By.id("com.android.calculator2:id/digit_1")).click();
        edr.findElement(By.id("com.android.calculator2:id/op_add")).click();
        edr.findElement(By.id("com.android.calculator2:id/digit_2")).click();
        edr.findElement(By.id("com.android.calculator2:id/digit_3")).click();

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(edr.findElement(By.id("com.android.calculator2:id/formula")).getText(), "11+23");
        softAssertion.assertEquals(edr.findElement(By.id("com.android.calculator2:id/result")).getText(), "34");

        edr.findElement(By.id("com.android.calculator2:id/eq")).click();

        softAssertion.assertEquals(driver.findElement(By.id("com.android.calculator2:id/result")).getText(), "34");

        edr.findElement(By.id("com.android.calculator2:id/toolbar")).click();
        edr.findElement(By.className("android.widget.ImageButton")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("android:id/title"))).click();

        edr.findElement(By.id("com.android.calculator2:id/mode")).click();

        softAssertion.assertEquals(edr.findElement(By.id("com.android.calculator2:id/result")).getText(), "34");

        softAssertion.assertAll();

        driver.close();
    }
}