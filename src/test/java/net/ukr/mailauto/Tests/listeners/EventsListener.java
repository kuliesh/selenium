package net.ukr.mailauto.Tests.listeners;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.io.File;
import java.io.IOException;

public class EventsListener extends AbstractWebDriverEventListener {

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println(("[Розпочато пошук локатора: "+by+"]"));
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println(("Локатор ["+ by +"] було знайдено"));
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        System.out.println(("[Виконано клік по елементу: " + element +"]"));
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        System.out.println("[Сталась помилка:] "+throwable.getMessage().split(":")[0]);


        File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(tempFile, new File(System.currentTimeMillis() + "screen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Знімок екрана помилки]");
    }
}
