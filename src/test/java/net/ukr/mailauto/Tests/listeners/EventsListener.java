package net.ukr.mailauto.Tests.listeners;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class EventsListener extends AbstractWebDriverEventListener {

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println(("[Розпочато пошук локатора:] " + by));
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println(("[Локатор"+ by +"]  було знайдено"));
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        System.out.println(("[Виконано клік по елементу]" + element));
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        // Do nothing
    }
}
