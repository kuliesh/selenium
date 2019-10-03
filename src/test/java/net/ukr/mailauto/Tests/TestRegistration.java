package net.ukr.mailauto.Tests;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.locks.Condition;

public class TestRegistration {

    static WebDriver driver;

    private static String baseUrl = "https://accounts.ukr.net/registration";

    private static String RGBColor = "rgb(219, 75, 55)";
    private static String RGBColorError = "rgba(219, 75, 55, 1)";

    private static String boardcolor = "border-color";
    private static String color = "color";

    private static String isInvalid = "input-default__input.is-size-normal.is-invalid";

    private static String errorUkr = "Поле має бути заповнене";
    private static String errorRu = "Поле должно быть заполнено";
    private static String errorEng = "You can’t leave this empty";

    private static int i=1000;

    @BeforeClass
    public static void setup () {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver ();
        driver.navigate().to(baseUrl);
    }

    @AfterClass
    public static void cleanup () {
        driver.quit();
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

    //* 1. перевірити, що зверху сторінки є вибір мови
    @Test
    public void choiceOfLanguage() {

        SoftAssert softAssertion = new SoftAssert();
        //Перевіряємо чи є елемент для вибору мови
        softAssertion.assertEquals(driver.findElement(By.cssSelector(".header__lang")).isDisplayed(), true, "Вибір мови не відобразився");

        //Первіряємо що для вибору є Українська мова .linkText
        softAssertion.assertEquals("Українська", driver.findElement(By.xpath("//button[1]/span[1]")).getText(), "Не знайшли Українську");

        //Первіряємо що для вибору є Українська мова
        softAssertion.assertEquals("Русский", driver.findElement(By.xpath("//button[2]/span[1]")).getText(), "Не знайшли Русский");

        //Первіряємо що для вибору є Українська мова
        Assert.assertEquals("English", driver.findElement(By.xpath("//button[3]/span[1]")).getText(), "Не знайшли English");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    //* 2. перевірити що поточна вибрана мова виділена кольором RGBa(52,56,64,1), а мови, які не вибрані - RGBa(102,153,0,1)
    @Test
    public void activeLanguage(){

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
        driver.navigate().refresh();
    }

    //Виконується для Української локалізації
    //3. перевірити, що після вводу перших символів імені скриньки з'являється підказка
    @Test
    public void tooltipForCreateName() throws InterruptedException {

        SoftAssert softAssertion = new SoftAssert();
        driver.findElement(By.id("id-login")).click();
        driver.findElement(By.id("id-login")).sendKeys("sds");
        //??Що потрібно використовувати, щоб не ставити тест на паузу, а тільки зявиться елемент - перевірити??
        Thread.sleep(i);
        //Перевіряємо, що відобразилась підсказка
        softAssertion.assertEquals(driver.findElement(By.cssSelector(".login-suggestions")).isDisplayed(), true, "Підсказка не відобразилася візуально");

        //Перевіряємо, що відобразилась підсказки та помилки саме з потрібним нам текстом:
        softAssertion.assertEquals("На жаль, скринька з таким іменем вже зайнята", driver.findElement(By.cssSelector(".login-suggestions__error")).getText(),
                "Помилка не відобразилася з потрібним текстом");

        softAssertion.assertEquals("Ми можемо запропонувати вам такі варіанти:", driver.findElement(By.cssSelector(".login-suggestions__title")).getText(),
                "Текст, що можемо запропоновати варіанти не відобразилася");
        //Перевіряємо що відобразився вся кількість підсказок - 6

        for (int i=1; i<=5; i++){
            softAssertion.assertEquals(driver.findElement(By.cssSelector("li.login-suggestions__item:nth-child("+i+")")).isDisplayed(), true,
                    "Підсказка"+i+" не відобразилася візуально"); //??Чогось на помилку ці тексти не виводяться??
        }

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    // 3.1. перевірити, що колір тексту помилки RGBa(219,75,55,1)
    @Test
    public void tooltipColorForError() throws InterruptedException {

        SoftAssert softAssertion = new SoftAssert();
        driver.findElement(By.id("id-login")).click();
        driver.findElement(By.id("id-login")).sendKeys("dfdf");
        Thread.sleep(i);
        softAssertion.assertEquals("rgba(219, 75, 55, 1)", driver.findElement(By.cssSelector(".login-suggestions__error")).getCssValue("color"),
                "Текст помилки не в кольорі RGBa(219,75,55,1)");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    // 3.2. колір тексту підказки RGBa(140,148,158,1)
    @Test
    public void tooltipColorText() throws InterruptedException {

        SoftAssert softAssertion = new SoftAssert();
        driver.findElement(By.id("id-login")).click();
        driver.findElement(By.id("id-login")).sendKeys("hghggf");
        Thread.sleep(i);

        softAssertion.assertEquals("rgba(140, 148, 158, 1)", driver.findElement(By.cssSelector(".login-suggestions__title")).getCssValue("color"),
                "Текст помилки не в кольорі RGBa(140,148,158,1)");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    // 3.3. колір тексту запропонованих варіантів RGBa(78,78,78,1)
    @Test
    public void tooltipColorAlternants() throws InterruptedException {

        SoftAssert softAssertion = new SoftAssert();
        driver.findElement(By.id("id-login")).click();
        driver.findElement(By.id("id-login")).sendKeys("sdsdsd");
        Thread.sleep(i);

        for (int i=1; i<=5; i++) {
            softAssertion.assertEquals("rgba(78, 78, 78, 1)", driver.findElement(By.cssSelector("li.login-suggestions__item:nth-child("+i+")")).getCssValue("color"),
                    "Текст помилки не в кольорі RGBa(78,78,78,1)");
        }
        softAssertion.assertAll();

        driver.navigate().refresh();
    }

    //4.перевірити, що в полях ім'я, прізвище,число, місяць, рік є відповідні підказки (в залежності від обраної мови)
    @Test
    public void personalDataUkr() {

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не Українська мова, тест зупинено");
        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо поле "Ім'я"
        softAssertion.assertEquals("Ім'я", driver.findElement(By.cssSelector("#id-first-name")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Ім'я'.");

        //Перевіряємо поле "Прізвище"
        softAssertion.assertEquals("Прізвище", driver.findElement(By.cssSelector("[tabindex='5']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Прізвище'.");

        //Перевіряємо поле "Число"
        softAssertion.assertEquals("число", driver.findElement(By.cssSelector("#id-birth-day")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'число'.");

        //Перевіряємо поле "Місяць"
        softAssertion.assertEquals("місяць", driver.findElement(By.cssSelector(".input-select.form__field.option-month")).getText(),
                "Невірна підсказка в полі 'місяць'.");

        //Перевіряємо поле "рік"
        softAssertion.assertEquals("рік", driver.findElement(By.cssSelector("[tabindex='8']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'рік'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void personalDataRu() {

        //Переходимо на російську локалізацію
        driver.findElement(By.xpath("//button[2]/span[1]")).click();

        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не росіська мова, тест зупинено");
        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо поле "Имя"
        softAssertion.assertEquals("Имя", driver.findElement(By.cssSelector("#id-first-name")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Имя'.");

        //Перевіряємо поле Фамилия"
        softAssertion.assertEquals("Фамилия", driver.findElement(By.cssSelector("[tabindex='5']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Фамилия'.");

        //Перевіряємо поле "число"
        softAssertion.assertEquals("число", driver.findElement(By.cssSelector("#id-birth-day")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'число'.");

        //Перевіряємо поле "месяц"
        softAssertion.assertEquals("месяц", driver.findElement(By.cssSelector(".input-select.form__field.option-month")).getText(),
                "Невірна підсказка в полі 'месяц'.");

        //Перевіряємо поле "год"
        softAssertion.assertEquals("год", driver.findElement(By.cssSelector("[tabindex='8']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'год'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void personalDataEn() {

        //Переходимо на англійську локалізацію
        driver.findElement(By.xpath("//button[3]/span[1]")).click();

        //Перевіряємо що мова англійська
        Assert.assertEquals("Create Your @UKR.NET Mailbox", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не англійська мова, тест зупинено");
        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо поле "First name"
        softAssertion.assertEquals("First name", driver.findElement(By.cssSelector("#id-first-name")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'First name'.");

        //Перевіряємо поле Last name"
        softAssertion.assertEquals("Last name", driver.findElement(By.cssSelector("[tabindex='5']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Last name'.");

        //Перевіряємо поле "Day"
        softAssertion.assertEquals("Day", driver.findElement(By.cssSelector("#id-birth-day")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Day'.");

        //Перевіряємо поле "Month"
        softAssertion.assertEquals("Month", driver.findElement(By.cssSelector(".input-select.form__field.option-month")).getText(),
                "Невірна підсказка в полі 'Month'.");

        //Перевіряємо поле "Year"
        softAssertion.assertEquals("Year", driver.findElement(By.cssSelector("[tabindex='8']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Year'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    /* 5. перевірити, що при спробі відправити не заповнену форму, усі поля підсвічуються червоним (RGBa(219,75,55,1))
    * і під ними присутній відповідний текст (в залежності від мови) який теж має червоний колір RGBa(219,75,55,1)
     */

    //?? Як покращити локатори??
    @Test
    public void boardColor() throws InterruptedException {

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не Українська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        driver.findElement(By.cssSelector(".verifier__send")).click();

        Thread.sleep(i);

        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо тест для української мови
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-login."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Придумайте ім'я поштової скриньки' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-password."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Придумайте пароль' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-password-repeat."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Введіть пароль повторно' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-first-name."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Ім'я' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("[tabindex='5']."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Прізвище' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-birth-day."+isInvalid)).getCssValue(boardcolor),
                "Границя 'число' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector(".option-month.is-invalid")).getCssValue(boardcolor),
                "Границя 'місяць' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("[tabindex='8']."+isInvalid)).getCssValue(boardcolor),
                "Границя 'рік' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector(".radio__imitator_invalid[for='id-sex-m']")).getCssValue(boardcolor),
                "Границя 'Чоловік' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
       softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector(".radio__imitator_invalid[for='id-sex-f']")).getCssValue(boardcolor),
                "Границя 'Жінка' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-sender-name."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Ім'я відправника' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-mobile."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Мобільний телефон' не виділена кольором "+RGBColor+" в результаті відсутності даних.");


        //Переходимо на російську локалізацію
        driver.findElement(By.xpath("//button[2]/span[1]")).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не російська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        driver.findElement(By.cssSelector(".verifier__send")).click();
        Thread.sleep(i);

        //Перевіряємо тест для російської мови
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-login."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Придумайте имя почтового ящика' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-password-repeat."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Введите пароль повторно' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector(".radio__imitator_invalid[for='id-sex-f']")).getCssValue(boardcolor),
                "Границя 'Женщина' не виділена кольором "+RGBColor+" в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, driver.findElement(By.cssSelector("#id-mobile."+isInvalid)).getCssValue(boardcolor),
                "Границя 'Мобильный телефон' не виділена кольором "+RGBColor+" в результаті відсутності даних.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }
    @Test
    public void textTextErrorColorWithoutDataUkr() throws InterruptedException {

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не Українська мова, тест зупинено.");
        //Ініціалізуємо тестовий сценарій
        driver.findElement(By.cssSelector(".verifier__send")).click();

        Thread.sleep(i);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(errorUkr, driver.findElement(By.xpath("//form/section[1]/div/p")).getText(),
                "Текст помилки '"+errorUkr+"' не відображається українською мовою для поля 'Придумайте ім'я поштової скриньки'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[1]/div/p")).getCssValue(color),
                "Текст помилки '"+errorUkr+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Придумайте ім'я поштової скриньки'.");

        softAssertion.assertEquals("Ви не зможете створити поштову скриньку без пароля", driver.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getText(),
                "Текст помилки 'Ви не зможете створити поштову скриньку без пароля' не відображається українською мовою для поля 'Придумайте пароль'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getCssValue(color),
                "Текст помилки 'Ви не зможете створити поштову скриньку без пароля' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Придумайте пароль'.");

        softAssertion.assertEquals("Ви не підтвердили новий пароль", driver.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getText(),
                "Текст помилки 'Ви не підтвердили новий пароль' не відображається українською мовою для поля 'Введіть пароль повторно'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getCssValue(color),
                "Текст помилки 'Ви не підтвердили новий пароль' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Введіть пароль повторно'.");

        softAssertion.assertEquals(errorUkr, driver.findElement(By.xpath("//form/section[3]/section[1]/p")).getText(),
                "Текст помилки '"+errorUkr+"' не відображається українською мовою для поля 'Як вас звати?'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[1]/p")).getCssValue(color),
                "Текст помилки '"+errorUkr+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Як вас звати?'.");

        softAssertion.assertEquals(errorUkr, driver.findElement(By.xpath("//form/section[3]/section[2]/p")).getText(),
                "Текст помилки '"+errorUkr+"' не відображається українською мовою для поля 'Дата народження'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[2]/p")).getCssValue(color),
                "Текст помилки '"+errorUkr+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Дата народженняє'.");

        softAssertion.assertEquals("Вкажіть стать", driver.findElement(By.xpath("//form/section[3]/section[3]/p")).getText(),
                "Текст помилки 'Вкажіть стать' не відображається українською мовою для вибору статі.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[3]/p")).getCssValue(color),
                "Текст помилки 'Вкажіть стать' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для вибору статі.");

        softAssertion.assertEquals(errorUkr, driver.findElement(By.xpath("//form/section[4]/div/p")).getText(),
                "Текст помилки '"+errorUkr+"' не відображається українською мовою для поля 'Ім'я відправника'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[4]/div/p")).getCssValue(color),
                "Текст помилки '"+errorUkr+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Ім'я відправника'.");

        softAssertion.assertEquals(errorUkr, driver.findElement(By.xpath("//form/section[6]/div/div/p")).getText(),
                "Текст помилки '"+errorUkr+"' не відображається українською мовою для поля 'Мобільний телефон'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[6]/div/div/p")).getCssValue(color),
                "Текст помилки '"+errorUkr+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Мобільний телефон.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void textTextErrorColorWithoutDataRu() throws InterruptedException {

        //Перемикаємося на російську мову
        driver.findElement(By.xpath("//button[2]/span[1]")).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не російська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        driver.findElement(By.cssSelector(".verifier__send")).click();

        Thread.sleep(i);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(errorRu, driver.findElement(By.xpath("//form/section[1]/div/p")).getText(),
                "Текст помилки '"+errorRu+"' не відображається російською мовою для поля 'Придумайте ім'я поштової скриньки'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[1]/div/p")).getCssValue(color),
                "Текст помилки '"+errorRu+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Придумайте ім'я поштової скриньки'.");

        softAssertion.assertEquals("Вы не сможете создать почтовый ящик без пароля", driver.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getText(),
                "Текст помилки 'Вы не сможете создать почтовый ящик без пароля' не відображається російською мовою для поля 'Придумайте пароль'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getCssValue(color),
                "Текст помилки 'Вы не сможете создать почтовый ящик без пароля' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Придумайте пароль'.");

        softAssertion.assertEquals("Вы не подтвердили новый пароль", driver.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getText(),
                "Текст помилки 'Вы не подтвердили новый пароль' не відображається російською мовою для поля 'Введіть пароль повторно'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getCssValue(color),
                "Текст помилки 'Вы не подтвердили новый пароль' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Введіть пароль повторно'.");

        softAssertion.assertEquals(errorRu, driver.findElement(By.xpath("//form/section[3]/section[1]/p")).getText(),
                "Текст помилки '"+errorRu+"' не відображається російською мовою для поля 'Як вас звати?'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[1]/p")).getCssValue(color),
                "Текст помилки '"+errorRu+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Як вас звати?'.");

        softAssertion.assertEquals(errorRu, driver.findElement(By.xpath("//form/section[3]/section[2]/p")).getText(),
                "Текст помилки '"+errorRu+"' не відображається російською мовою для поля 'Дата народження'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[2]/p")).getCssValue(color),
                "Текст помилки '"+errorRu+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Дата народженняє'.");

        softAssertion.assertEquals("Укажите пол", driver.findElement(By.xpath("//form/section[3]/section[3]/p")).getText(),
                "Текст помилки 'Укажите пол' не відображається російською мовою для вибору статі.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[3]/p")).getCssValue(color),
                "Текст помилки 'Укажите пол' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для вибору статі.");

        softAssertion.assertEquals(errorRu, driver.findElement(By.xpath("//form/section[4]/div/p")).getText(),
                "Текст помилки '"+errorRu+"' не відображається російською мовою для поля 'Ім'я відправника'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[4]/div/p")).getCssValue(color),
                "Текст помилки '"+errorRu+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Ім'я відправника'.");

        softAssertion.assertEquals(errorRu, driver.findElement(By.xpath("//form/section[6]/div/div/p")).getText(),
                "Текст помилки '"+errorRu+"' не відображається російською мовою для поля 'Мобільний телефон'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[6]/div/div/p")).getCssValue(color),
                "Текст помилки '"+errorRu+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Мобільний телефон.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void textTextErrorColorWithoutDataEng() throws InterruptedException {

        //Перемикаємося на англійську мову
        driver.findElement(By.xpath("//button[3]/span[1]")).click();
        //Перевіряємо що мова англійська
        Assert.assertEquals("Create Your @UKR.NET Mailbox", driver.findElement(By.cssSelector("h1.header-title")).getText(),
                "Вибрана не англійська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        driver.findElement(By.cssSelector(".verifier__send")).click();

        Thread.sleep(i);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(errorEng, driver.findElement(By.xpath("//form/section[1]/div/p")).getText(),
                "Текст помилки '"+errorEng+"' не відображається англійською мовою для поля 'Придумайте ім'я поштової скриньки'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[1]/div/p")).getCssValue(color),
                "Текст помилки '"+errorEng+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Придумайте ім'я поштової скриньки'.");

        softAssertion.assertEquals("You need to think of a password to create a mailbox", driver.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getText(),
                "Текст помилки 'You need to think of a password to create a mailbox' не відображається англійською мовою для поля 'Придумайте пароль'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getCssValue(color),
                "Текст помилки 'You need to think of a password to create a mailbox' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Придумайте пароль'.");

        softAssertion.assertEquals("Please confirm your new password", driver.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getText(),
                "Текст помилки 'Please confirm your new password' не відображається англійською мовою для поля 'Введіть пароль повторно'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getCssValue(color),
                "Текст помилки 'Please confirm your new password' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Введіть пароль повторно'.");

        softAssertion.assertEquals(errorEng, driver.findElement(By.xpath("//form/section[3]/section[1]/p")).getText(),
                "Текст помилки '"+errorEng+"' не відображається англійською мовою для поля 'Як вас звати?'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[1]/p")).getCssValue(color),
                "Текст помилки '"+errorEng+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Як вас звати?'.");

        softAssertion.assertEquals(errorEng, driver.findElement(By.xpath("//form/section[3]/section[2]/p")).getText(),
                "Текст помилки '"+errorEng+"' не відображається англійською мовою для поля 'Дата народження'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[2]/p")).getCssValue(color),
                "Текст помилки '"+errorEng+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Дата народженняє'.");

        softAssertion.assertEquals("Please specify your gender", driver.findElement(By.xpath("//form/section[3]/section[3]/p")).getText(),
                "Текст помилки 'Please specify your gender' не відображається англійською мовою для вибору статі.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[3]/section[3]/p")).getCssValue(color),
                "Текст помилки 'Please specify your gender' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для вибору статі.");

        softAssertion.assertEquals(errorEng, driver.findElement(By.xpath("//form/section[4]/div/p")).getText(),
                "Текст помилки '"+errorEng+"' не відображається англійською мовою для поля 'Ім'я відправника'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[4]/div/p")).getCssValue(color),
                "Текст помилки '"+errorEng+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Ім'я відправника'.");

        softAssertion.assertEquals(errorEng, driver.findElement(By.xpath("//form/section[6]/div/div/p")).getText(),
                "Текст помилки '"+errorEng+"' не відображається англійською мовою для поля 'Мобільний телефон'.");
        softAssertion.assertEquals(RGBColorError, driver.findElement(By.xpath("//form/section[6]/div/div/p")).getCssValue(color),
                "Текст помилки '"+errorEng+"' не відображається  кольором "+RGBColorError+" в результаті відсутності даних для поля 'Мобільний телефон.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }
}