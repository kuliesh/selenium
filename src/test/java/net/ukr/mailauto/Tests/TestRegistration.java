package net.ukr.mailauto.Tests;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestRegistration {

    static WebDriver driver;

    String baseUrl = "https://accounts.ukr.net/registration";

    @BeforeClass
    public static void setup () {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver ();
    }
    @AfterClass
    public static void cleanup () {
        driver.quit ();
    }

    /*
    * 1. перевірити, що зверху сторінки є вибір мови
    * 2. перевірити що поточна вибрана мова виділена кольором RGBa(52,56,64,1), а мови, які не вибрані - RGBa(102,153,0,1)
    * 3. перевірити, що після вводу перших символів імені скриньки з'являється підказка:
    * 3.1. перевірити, що колір тексту помилки RGBa(219,75,55,1)
    * 3.2. колір тексту підказки RGBa(140,148,158,1)
    * 3.3. колір тексту запропонованих варіантів RGBa(78,78,78,1)
    * 4. перевірити, що в полях ім'я, прізвище,число, місяць, рік є відповідні підказки (в залежності від обраної мови)
    * 5. перевірити, що при спробі відправити не заповнену форму, усі поля підсвічуються червоним (RGBa(219,75,55,1))
    * і під ними присутній відповідний текст (в залежності від мови) який теж має червоний колір RGBa(219,75,55,1)
    */

//Assert.assertEquals(driver.findElement(By.className(".login-button__user")).getText(), login+"@ukr.net");

    //* 1. перевірити, що зверху сторінки є вибір мови
    @Test
    public void choiceOfLanguage() {
        driver.navigate().to(baseUrl);

        SoftAssert softAssertion = new SoftAssert();
        //Перевіряємо чи є елемент для вибору мови
        softAssertion.assertEquals(driver.findElement(By.cssSelector(".header__lang")).isDisplayed(), true);

        //Первіряємо що для вибору є Українська мова
        softAssertion.assertEquals("Українська", driver.findElement(By.xpath("//button[1]/span[1]")).getText(), "Не знайшли Українську");

        //Первіряємо що для вибору є Українська мова
        softAssertion.assertEquals("Русский", driver.findElement(By.xpath("//button[2]/span[1]")).getText(), "Не знайшли Русский");

        //Первіряємо що для вибору є Українська мова
        Assert.assertEquals("English", driver.findElement(By.xpath("//button[3]/span[1]")).getText(), "Не знайшли English");

        softAssertion.assertAll();
    }

    //* 2. перевірити що поточна вибрана мова виділена кольором RGBa(52,56,64,1), а мови, які не вибрані - RGBa(102,153,0,1)

    @Test
    public void activeLanguage(){
        driver.navigate().to(baseUrl);

        SoftAssert softAssertion = new SoftAssert();

        //Виконуємо клік по "Русский"
        driver.findElement(By.xpath("//button[2]/span[1]")).click();
        //Перевіряємо що перемкнулися на російську мову !!Не зумів прив'язати класс header__lang-item header__lang-item_active!!
        Assert.assertEquals("Регистрация почтового ящика", driver.findElement(By.cssSelector("h1.header-title")).getText(), "Не російська мова");

        //Перевіряємо, що кнопка Россійській виділена кольором RGBa(52,56,64,1)
        softAssertion.assertEquals("rgba(52, 56, 64, 1)", driver.findElement(By.xpath("//button[2]/span[1]")).getCssValue("color"),
                "Російська кнопка не в кольорі RGBa(52,56,64,1)");

        //Перевіряємо, що кнопка Українська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", driver.findElement(By.xpath("//button[1]/span[1]")).getCssValue("color"),
                "Українська кнопка не в кольорі RGBa(102,153,0,1)");

        //Перевіряємо, що кнопка Англіська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", driver.findElement(By.xpath("//button[3]/span[1]")).getCssValue("color"),
                "Англійська кнопка не в кольорі RGBa(102,153,0,1)");

        //Щоб перевірити, що у насзмінюється колір активної кнопки
        //Вибиремо тепер Українську локалізацію і перевіремо, що колір кнопок змінюється
        driver.findElement(By.xpath("//button[1]/span[1]")).click();

        //Перевіряємо, що кнопка Українська виділена кольором RGBa(52,56,64,1)
        softAssertion.assertEquals("rgba(52, 56, 64, 1)", driver.findElement(By.xpath("//button[1]/span[1]")).getCssValue("color"),
                "Українська кнопка не в кольорі RGBa(52,56,64,1)");

        //Перевіряємо, що кнопка Російська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", driver.findElement(By.xpath("//button[2]/span[1]")).getCssValue("color"),
                "Російська кнопка не в кольорі RGBa(102,153,0,1)");

        //Перевіряємо, що кнопка РАнглійська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", driver.findElement(By.xpath("//button[3]/span[1]")).getCssValue("color"),
                "Анлійська кнопка не в кольорі RGBa(102,153,0,1)");

        softAssertion.assertAll();
    }

}
