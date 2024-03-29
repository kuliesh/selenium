package net.ukr.mailauto.Tests;

import io.github.bonigarcia.wdm.WebDriverManager;

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
import java.util.concurrent.TimeUnit;

public class TestRegistration extends EventsListener {

    static WebDriver driver;
    private static EventFiringWebDriver edr;
    private static WebDriverWait wait;

    private static String baseUrl = "https://accounts.ukr.net/registration";
    private static String ukrnetUrl = "https://www.ukr.net";
    private static String ukrmailUrl = "https://mail.ukr.net";

    String RGBColor = "rgb(219, 75, 55)";
    String RGBColorError = "rgba(219, 75, 55, 2)";

    String boardcolor = "border-color";
    String color = "color";

    private static String isInvalid = "input-default__input.is-size-normal.is-invalid";

    private static String errorUkr = "Поле має бути заповнене";
    private static String errorRu = "Поле должно быть заполнено";
    private static String errorEng = "You can’t leave this empty";

    public static String UA = "button:nth-of-type(1) > .header__lang-long-name";
    public static String RU = "button:nth-of-type(2) > .header__lang-long-name";
    public static String EN = "button:nth-of-type(3) > .header__lang-long-name";

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

    public ExpectedCondition<String> anyWindowOtherThan(Set<String> windows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver input) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(windows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }
    //* 1. перевірити, що зверху сторінки є вибір мови
    @Test
    public void testChoiceOfLanguage() {

        SoftAssert softAssertion = new SoftAssert();
        //Перевіряємо чи є елемент для вибору мови
        softAssertion.assertTrue(edr.findElement(By.cssSelector(".header__lang")).isDisplayed(), "Вибір мови не відобразився");

        //Первіряємо що для вибору є Українська мова .linkText
        softAssertion.assertEquals("Українська", edr.findElement(By.cssSelector(UA)).getText(), "Не знайшли Українську");

        //Первіряємо що для вибору є Українська мова
        softAssertion.assertEquals("Русский", edr.findElement(By.cssSelector(RU)).getText(), "Не знайшли Русский");

        //Первіряємо що для вибору є Українська мова
        Assert.assertEquals("English", edr.findElement(By.cssSelector(EN)).getText(), "Не знайшли English");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    //* 2. перевірити що поточна вибрана мова виділена кольором RGBa(52,56,64,1), а мови, які не вибрані - RGBa(102,153,0,1)
    @Test
    public void testActiveLanguage() {

        SoftAssert softAssertion = new SoftAssert();

        //Виконуємо клік по "Русский"
        edr.findElement(By.cssSelector(RU)).click();
        //Перевіряємо що перемкнулися на російську мову !!Не зумів прив'язати класс header__lang-item header__lang-item_active!!
        Assert.assertEquals("Регистрация почтового ящика", edr.findElement(By.cssSelector(verLang)).getText(), "Не російська мова");

        //Перевіряємо, що кнопка Россійській виділена кольором RGBa(52,56,64,1)
        softAssertion.assertEquals("rgba(52, 56, 64, 1)", edr.findElement(By.cssSelector(RU)).getCssValue("color"),
                "Російська кнопка не в кольорі RGBa(52,56,64,1)");

        //Перевіряємо, що кнопка Українська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", edr.findElement(By.cssSelector(UA)).getCssValue("color"),
                "Українська кнопка не в кольорі RGBa(102,153,0,1)");

        //Перевіряємо, що кнопка Англіська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", edr.findElement(By.cssSelector(EN)).getCssValue("color"),
                "Англійська кнопка не в кольорі RGBa(102,153,0,1)");

        //Щоб перевірити, що у насзмінюється колір активної кнопки
        //Вибиремо тепер Українську локалізацію і перевіремо, що колір кнопок змінюється
        edr.findElement(By.cssSelector(UA)).click();

        //Перевіряємо, що кнопка Українська виділена кольором RGBa(52,56,64,1)
        softAssertion.assertEquals("rgba(52, 56, 64, 1)", edr.findElement(By.cssSelector(UA)).getCssValue("color"),
                "Українська кнопка не в кольорі RGBa(52,56,64,1)");

        //Перевіряємо, що кнопка Російська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", edr.findElement(By.cssSelector(RU)).getCssValue("color"),
                "Російська кнопка не в кольорі RGBa(102,153,0,1)");

        //Перевіряємо, що кнопка РАнглійська виділена кольором RGBa(102,153,0,1)
        softAssertion.assertEquals("rgba(102, 153, 0, 1)", edr.findElement(By.cssSelector(EN)).getCssValue("color"),
                "Анлійська кнопка не в кольорі RGBa(102,153,0,1)");

        softAssertion.assertAll();
        edr.navigate().refresh();
    }

    //Виконується для Української локалізації
    //3. перевірити, що після вводу перших символів імені скриньки з'являється підказка
    @Test
    public void testTooltipForCreateName() {

        SoftAssert softAssertion = new SoftAssert();
        edr.findElement(By.id("id-login")).click();
        edr.findElement(By.id("id-login")).sendKeys("sds");
        //??Що потрібно використовувати, щоб не ставити тест на паузу, а тільки зявиться елемент - перевірити??
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);
        //Перевіряємо, що відобразилась підсказка
        softAssertion.assertTrue(edr.findElement(By.className("login-suggestions")).isDisplayed(), "Підсказка не відобразилася візуально");

        //Перевіряємо, що відобразилась підсказки та помилки саме з потрібним нам текстом:
        softAssertion.assertEquals("На жаль, скринька з таким іменем вже зайнята", edr.findElement(By.className("login-suggestions__error")).getText(),
                "Помилка не відобразилася з потрібним текстом");

        softAssertion.assertEquals("Ми можемо запропонувати вам такі варіанти:", edr.findElement(By.className("login-suggestions__title")).getText(),
                "Текст, що можемо запропоновати варіанти не відобразилася");

        //Перевіряємо що відобразився вся кількість підсказок - 6
        for (int i = 1; i <= 5; i++) {
            softAssertion.assertTrue(edr.findElement(By.cssSelector("li.login-suggestions__item:nth-child(" + i + ")")).isDisplayed(),
                    "Підсказка" + i + " не відобразилася візуально"); //??Чогось на помилку ці тексти не виводяться??
        }

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    // 3.1. перевірити, що колір тексту помилки RGBa(219,75,55,1)
    @Test
    public void testTooltipColorForError() {

        SoftAssert softAssertion = new SoftAssert();
        edr.findElement(By.id("id-login")).click();
        edr.findElement(By.id("id-login")).sendKeys("dfdf");
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.className("login-suggestions__error")).getCssValue("color"),
                "Текст помилки не в кольорі " + RGBColorError + ".");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    // 3.2. колір тексту підказки RGBa(140,148,158,1)
    @Test
    public void testTooltipColorText() {

        SoftAssert softAssertion = new SoftAssert();
        edr.findElement(By.id("id-login")).click();
        edr.findElement(By.id("id-login")).sendKeys("hghggf");
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);

        softAssertion.assertEquals("rgba(140, 148, 158, 1)", edr.findElement(By.className("login-suggestions__title")).getCssValue("color"),
                "Текст підсказки не в кольорі RGBa(140,148,158,1)");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    // 3.3. колір тексту запропонованих варіантів RGBa(78,78,78,1)
    @Test
    public void testTooltipColorAlternants() {

        SoftAssert softAssertion = new SoftAssert();
        edr.findElement(By.id("id-login")).click();
        edr.findElement(By.id("id-login")).sendKeys("sdsdsd");
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);

        for (int i = 1; i <= 5; i++) {
            softAssertion.assertEquals("rgba(78, 78, 78, 1)", edr.findElement(By.cssSelector("li.login-suggestions__item:nth-child(" + i + ")")).getCssValue("color"),
                    "Текст варіанту не в кольорі RGBa(78,78,78,1)");
        }
        softAssertion.assertAll();

        driver.navigate().refresh();
    }

    //4.перевірити, що в полях ім'я, прізвище,число, місяць, рік є відповідні підказки (в залежності від обраної мови)
    @Test
    public void testPersonalDataUkr() {

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не Українська мова, тест зупинено");
        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо поле "Ім'я"
        softAssertion.assertEquals("Ім'я", edr.findElement(By.id("id-first-name")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Ім'я'.");

        //Перевіряємо поле "Прізвище"
        softAssertion.assertEquals("Прізвище", edr.findElement(By.cssSelector("[tabindex='5']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Прізвище'.");

        //Перевіряємо поле "Число"
        softAssertion.assertEquals("число", edr.findElement(By.id("id-birth-day")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'число'.");

        //Перевіряємо поле "Місяць"
        softAssertion.assertEquals("місяць", edr.findElement(By.cssSelector(".input-select.form__field.option-month")).getText(),
                "Невірна підсказка в полі 'місяць'.");

        //Перевіряємо поле "рік"
        softAssertion.assertEquals("рік", edr.findElement(By.cssSelector("[tabindex='8']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'рік'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void testPersonalDataRu() {

        //Переходимо на російську локалізацію
        edr.findElement(By.cssSelector(RU)).click();

        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не росіська мова, тест зупинено");
        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо поле "Имя"
        softAssertion.assertEquals("Имя", edr.findElement(By.id("id-first-name")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Имя'.");

        //Перевіряємо поле Фамилия"
        softAssertion.assertEquals("Фамилия", edr.findElement(By.cssSelector("[tabindex='5']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Фамилия'.");

        //Перевіряємо поле "число"
        softAssertion.assertEquals("число", edr.findElement(By.id("id-birth-day")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'число'.");

        //Перевіряємо поле "месяц"
        softAssertion.assertEquals("месяц", edr.findElement(By.cssSelector(".input-select.form__field.option-month")).getText(),
                "Невірна підсказка в полі 'месяц'.");

        //Перевіряємо поле "год"
        softAssertion.assertEquals("год", edr.findElement(By.cssSelector("[tabindex='8']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'год'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void testPersonalDataEn() {

        //Переходимо на англійську локалізацію
        edr.findElement(By.cssSelector(EN)).click();

        //Перевіряємо що мова англійська
        Assert.assertEquals("Create Your @UKR.NET Mailbox", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не англійська мова, тест зупинено");
        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо поле "First name"
        softAssertion.assertEquals("First name", edr.findElement(By.id("id-first-name")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'First name'.");

        //Перевіряємо поле Last name"
        softAssertion.assertEquals("Last name", edr.findElement(By.cssSelector("[tabindex='5']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Last name'.");

        //Перевіряємо поле "Day"
        softAssertion.assertEquals("Day", edr.findElement(By.id("id-birth-day")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Day'.");

        //Перевіряємо поле "Month"
        softAssertion.assertEquals("Month", edr.findElement(By.cssSelector(".input-select.form__field.option-month")).getText(),
                "Невірна підсказка в полі 'Month'.");

        //Перевіряємо поле "Year"
        softAssertion.assertEquals("Year", edr.findElement(By.cssSelector("[tabindex='8']")).getAttribute("placeholder"),
                "Невірна підсказка в полі 'Year'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    /* 5. перевірити, що при спробі відправити не заповнену форму, усі поля підсвічуються червоним (RGBa(219,75,55,1))
     * і під ними присутній відповідний текст (в залежності від мови) який теж має червоний колір RGBa(219,75,55,1)
     */

    //?? Як покращити локатори??
    @Test
    public void testBoardColor() throws InterruptedException {

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не Українська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        edr.findElement(By.className("verifier__send")).click();

        Thread.sleep(5000);

        SoftAssert softAssertion = new SoftAssert();

        //Перевіряємо тест для української мови
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-login." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Придумайте ім'я поштової скриньки' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-password." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Придумайте пароль' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-password-repeat." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Введіть пароль повторно' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-first-name." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Ім'я' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("[tabindex='5']." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Прізвище' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-birth-day." + isInvalid)).getCssValue(boardcolor),
                "Границя 'число' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector(".option-month.is-invalid")).getCssValue(boardcolor),
                "Границя 'місяць' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("[tabindex='8']." + isInvalid)).getCssValue(boardcolor),
                "Границя 'рік' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector(".radio__imitator_invalid[for='id-sex-m']")).getCssValue(boardcolor),
                "Границя 'Чоловік' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector(".radio__imitator_invalid[for='id-sex-f']")).getCssValue(boardcolor),
                "Границя 'Жінка' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-sender-name." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Ім'я відправника' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-mobile." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Мобільний телефон' не виділена кольором " + RGBColor + " в результаті відсутності даних.");


        //Переходимо на російську локалізацію
        edr.findElement(By.cssSelector(RU)).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не російська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        edr.findElement(By.className("verifier__send")).click();
        Thread.sleep(5000);

        //Перевіряємо тест для російської мови
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-login." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Придумайте имя почтового ящика' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-password-repeat." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Введите пароль повторно' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector(".radio__imitator_invalid[for='id-sex-f']")).getCssValue(boardcolor),
                "Границя 'Женщина' не виділена кольором " + RGBColor + " в результаті відсутності даних.");
        softAssertion.assertEquals(RGBColor, edr.findElement(By.cssSelector("#id-mobile." + isInvalid)).getCssValue(boardcolor),
                "Границя 'Мобильный телефон' не виділена кольором " + RGBColor + " в результаті відсутності даних.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void testTextErrorColorWithoutDataUkr() {

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не Українська мова, тест зупинено.");
        //Ініціалізуємо тестовий сценарій
        edr.findElement(By.className("verifier__send")).click();
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(errorUkr, edr.findElement(By.xpath("//form/section[1]/div/p")).getText(),
                "Текст помилки '" + errorUkr + "' не відображається українською мовою для поля 'Придумайте ім'я поштової скриньки'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[1]/div/p")).getCssValue(color),
                "Текст помилки '" + errorUkr + "' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Придумайте ім'я поштової скриньки'.");

        softAssertion.assertEquals("Ви не зможете створити поштову скриньку без пароля", edr.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getText(),
                "Текст помилки 'Ви не зможете створити поштову скриньку без пароля' не відображається українською мовою для поля 'Придумайте пароль'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getCssValue(color),
                "Текст помилки 'Ви не зможете створити поштову скриньку без пароля' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Придумайте пароль'.");

        softAssertion.assertEquals("Ви не підтвердили новий пароль", edr.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getText(),
                "Текст помилки 'Ви не підтвердили новий пароль' не відображається українською мовою для поля 'Введіть пароль повторно'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getCssValue(color),
                "Текст помилки 'Ви не підтвердили новий пароль' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Введіть пароль повторно'.");

        softAssertion.assertEquals(errorUkr, edr.findElement(By.xpath("//form/section[3]/section[1]/p")).getText(),
                "Текст помилки '" + errorUkr + "' не відображається українською мовою для поля 'Як вас звати?'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[3]/section[1]/p")).getCssValue(color),
                "Текст помилки '" + errorUkr + "' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Як вас звати?'.");

        softAssertion.assertEquals(errorUkr, edr.findElement(By.xpath("//form/section[3]/section[2]/p")).getText(),
                "Текст помилки '" + errorUkr + "' не відображається українською мовою для поля 'Дата народження'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[3]/section[2]/p")).getCssValue(color),
                "Текст помилки '" + errorUkr + "' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Дата народженняє'.");

        softAssertion.assertEquals("Вкажіть стать", edr.findElement(By.xpath("//form/section[3]/section[3]/p")).getText(),
                "Текст помилки 'Вкажіть стать' не відображається українською мовою для вибору статі.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[3]/section[3]/p")).getCssValue(color),
                "Текст помилки 'Вкажіть стать' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для вибору статі.");

        softAssertion.assertEquals(errorUkr, edr.findElement(By.xpath("//form/section[4]/div/p")).getText(),
                "Текст помилки '" + errorUkr + "' не відображається українською мовою для поля 'Ім'я відправника'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[4]/div/p")).getCssValue(color),
                "Текст помилки '" + errorUkr + "' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Ім'я відправника'.");

        softAssertion.assertEquals(errorUkr, edr.findElement(By.xpath("//form/section[6]/div/div/p")).getText(),
                "Текст помилки '" + errorUkr + "' не відображається українською мовою для поля 'Мобільний телефон'.");
        softAssertion.assertEquals(RGBColorError, edr.findElement(By.xpath("//form/section[6]/div/div/p")).getCssValue(color),
                "Текст помилки '" + errorUkr + "' не відображається  кольором " + RGBColorError + " в результаті відсутності даних для поля 'Мобільний телефон.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void testTextErrorColorWithoutDataRu() {

        //Перемикаємося на російську мову
        edr.findElement(By.cssSelector(RU)).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не російська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        edr.findElement(By.className("verifier__send")).click();
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(errorRu, edr.findElement(By.xpath("//form/section[1]/div/p")).getText(),
                "Текст помилки '" + errorRu + "' не відображається російською мовою для поля 'Придумайте ім'я поштової скриньки'.");

        softAssertion.assertEquals("Вы не сможете создать почтовый ящик без пароля", edr.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getText(),
                "Текст помилки 'Вы не сможете создать почтовый ящик без пароля' не відображається російською мовою для поля 'Придумайте пароль'.");

        softAssertion.assertEquals("Вы не подтвердили новый пароль", edr.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getText(),
                "Текст помилки 'Вы не подтвердили новый пароль' не відображається російською мовою для поля 'Введіть пароль повторно'.");

        softAssertion.assertEquals(errorRu, edr.findElement(By.xpath("//form/section[3]/section[1]/p")).getText(),
                "Текст помилки '" + errorRu + "' не відображається російською мовою для поля 'Як вас звати?'.");

        softAssertion.assertEquals(errorRu, edr.findElement(By.xpath("//form/section[3]/section[2]/p")).getText(),
                "Текст помилки '" + errorRu + "' не відображається російською мовою для поля 'Дата народження'.");

        softAssertion.assertEquals("Укажите пол", edr.findElement(By.xpath("//form/section[3]/section[3]/p")).getText(),
                "Текст помилки 'Укажите пол' не відображається російською мовою для вибору статі.");

        softAssertion.assertEquals(errorRu, edr.findElement(By.xpath("//form/section[4]/div/p")).getText(),
                "Текст помилки '" + errorRu + "' не відображається російською мовою для поля 'Ім'я відправника'.");

        softAssertion.assertEquals(errorRu, edr.findElement(By.xpath("//form/section[6]/div/div/p")).getText(),
                "Текст помилки '" + errorRu + "' не відображається російською мовою для поля 'Мобільний телефон'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    @Test
    public void testTextErrorColorWithoutDataEng() {

        //Перемикаємося на англійську мову
        edr.findElement(By.cssSelector(EN)).click();
        //Перевіряємо що мова англійська
        Assert.assertEquals("Create Your @UKR.NET Mailbox", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не англійська мова, тест зупинено");
        //Ініціалізуємо тестовий сценарій
        edr.findElement(By.className("verifier__send")).click();
        driver.manage().timeouts().implicitlyWait(i, TimeUnit.SECONDS);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(errorEng, edr.findElement(By.xpath("//form/section[1]/div/p")).getText(),
                "Текст помилки '" + errorEng + "' не відображається англійською мовою для поля 'Придумайте ім'я поштової скриньки'.");

        softAssertion.assertEquals("You need to think of a password to create a mailbox", edr.findElement(By.xpath("//form/section[2]/div[1]/div/p")).getText(),
                "Текст помилки 'You need to think of a password to create a mailbox' не відображається англійською мовою для поля 'Придумайте пароль'.");

        softAssertion.assertEquals("Please confirm your new password", edr.findElement(By.xpath("//form/section[2]/div[2]/div/p")).getText(),
                "Текст помилки 'Please confirm your new password' не відображається англійською мовою для поля 'Введіть пароль повторно'.");

        softAssertion.assertEquals(errorEng, edr.findElement(By.xpath("//form/section[3]/section[1]/p")).getText(),
                "Текст помилки '" + errorEng + "' не відображається англійською мовою для поля 'Як вас звати?'.");

        softAssertion.assertEquals(errorEng, edr.findElement(By.xpath("//form/section[3]/section[2]/p")).getText(),
                "Текст помилки '" + errorEng + "' не відображається англійською мовою для поля 'Дата народження'.");

        softAssertion.assertEquals("Please specify your gender", edr.findElement(By.xpath("//form/section[3]/section[3]/p")).getText(),
                "Текст помилки 'Please specify your gender' не відображається англійською мовою для вибору статі.");
        softAssertion.assertEquals(errorEng, edr.findElement(By.xpath("//form/section[4]/div/p")).getText(),
                "Текст помилки '" + errorEng + "' не відображається англійською мовою для поля 'Ім'я відправника'.");

        softAssertion.assertEquals(errorEng, edr.findElement(By.xpath("//form/section[6]/div/div/p")).getText(),
                "Текст помилки '" + errorEng + "' не відображається англійською мовою для поля 'Мобільний телефон'.");

        softAssertion.assertAll();
        driver.navigate().refresh();
    }

    //6.1. Тест для перевірки, що по кліку на "угоду про конфіденційність" відкривається угода про конфіденційність для укр локалізації
    @Test
    public void testVerifiedPrivacePoliciURLUA() {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не Українська мова, тест зупинено.");
        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector("form [data-tooltip]")).click();

        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        String logoUA = edr.findElement(By.cssSelector("img")).getAttribute("src");

        //Пеервіряємо тестові дані
        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrnetUrl + "/terms/", driver.getCurrentUrl(),
                "Посилання сторінки terms не відповідає Українській локалізації.");

        softAssertion.assertEquals(ukrnetUrl + "/img/terms-logo-ua.gif", logoUA,
                "Лого не відповідає Українській локалізації.");

        softAssertion.assertEquals("Угода про конфіденційність", edr.findElement(By.cssSelector(".article > h2")).getText(),
                "Текст заголовку сторінки terms не відповідає Українській локалізації.");

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }

    //6.2. Тест для перевірки, що по кліку на "угоду про конфіденційність" відкривається угода про конфіденційність для рос локалізації
    @Test
    public void testVerifiedPrivacePoliciURLRU() {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        edr.findElement(By.cssSelector(RU)).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не російська мова, тест зупинено");

        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector("form [data-tooltip]")).click();

        //перемикаємо фокус WebDriver на нщойно відкрити вкладку
        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        String logoRU = edr.findElement(By.cssSelector("img")).getAttribute("src");

        //Пеервіряємо тестові дані
        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrnetUrl + "/ru/terms/", driver.getCurrentUrl(),
                "Посилання сторінки terms не відповідає Російській локалізації.");

        softAssertion.assertEquals(ukrnetUrl + "/img/terms-logo-ru.gif", logoRU,
                "Лого не відповідає Російській локалізації.");

        softAssertion.assertEquals("Соглашение о конфиденциальности", edr.findElement(By.cssSelector(".article > h2")).getText(),
                "Текст заголовку сторінки terms не відповідає Російській локалізації.");

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }

    //6.3. Тест для перевірки, що по кліку на "угоду про конфіденційність" відкривається угода про конфіденційність для англ локалізації
    @Test
    public void testVerifiedPrivacePoliciURLEng() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        edr.findElement(By.cssSelector(EN)).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Create Your @UKR.NET Mailbox", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не англійська мова, тест зупинено");

        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector("form [data-tooltip]")).click();

        //перемикаємо фокус WebDriver на нщойно відкрити вкладку
        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        String logoRU = edr.findElement(By.cssSelector("img")).getAttribute("src");

        //Пеервіряємо тестові дані
        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrnetUrl + "/terms/", driver.getCurrentUrl(),
                "Посилання сторінки terms не відповідає Англійській локалізації.");

        softAssertion.assertEquals(ukrnetUrl + "/img/terms-logo-ua.gif", logoRU,
                "Лого не відповідає Англійській локалізації.");

        softAssertion.assertEquals("Угода про конфіденційність", edr.findElement(By.cssSelector(".article > h2")).getText(),
                "Текст заголовку сторінки terms не відповідає Англійській локалізації.");

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }


    //7.1. Тест для перевірки, що по кліку на "угоду про використання" відкривається угода про використання для української локалізації
    @Test
    public void testOpenTermOfServiceUA() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //Перевіряємо що мова Українська
        Assert.assertEquals("Реєстрація поштової скриньки", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не Українська мова, тест зупинено.");
        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector(".confirm-terms [data-tooltip]")).click();

        //перемикаємо фокус WebDriver на нщойно відкрити вкладку
        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrmailUrl + "/terms_uk.html", driver.getCurrentUrl(),
                "Посилання сторінки 'Угоди використання' не відповідає Українській локалізації.");

        softAssertion.assertEquals("Угода про використання електронної пошти FREEMAIL (mail.ukr.net)", edr.findElement(By.cssSelector(".register > h3")).getText(),
                "Текст заголовку сторінки terms не відповідає Українській локалізації.");

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }

    //7.2. Тест для перевірки, що по кліку на "угоду про використання" відкривається угода про використання для рос локалізації
    @Test
    public void testOpenTermOfServiceRU() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        edr.findElement(By.cssSelector(RU)).click();
        //Перевіряємо що мова російська
        Assert.assertEquals("Регистрация почтового ящика", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не російська мова, тест зупинено");

        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector(".confirm-terms [data-tooltip]")).click();

        //перемикаємо фокус WebDriver на нщойно відкрити вкладку
        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrmailUrl + "/terms_ru.html", driver.getCurrentUrl(),
                "Посилання сторінки 'Угоди використання' не відповідає Українській локалізації.");

        softAssertion.assertEquals("Соглашение об использовании электронной почты FREEMAIL (mail.ukr.net)", edr.findElement(By.cssSelector(".register > h3")).getText(),
                "Текст заголовку сторінки terms не відповідає Українській локалізації.");

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }

    //7.3. Тест для перевірки, що по кліку на "угоду про використання" відкривається угода про використання для англ локалізації
    @Test
    public void testOpenTermOfServiceEN() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        edr.findElement(By.cssSelector(EN)).click();
        //Перевіряємо що мова англійська
        Assert.assertEquals("Create Your @UKR.NET Mailbox", edr.findElement(By.cssSelector(verLang)).getText(),
                "Вибрана не англійська мова, тест зупинено");

        //Ініціалізуємо тестовий сценарій
        Set<String> exitWindows = driver.getWindowHandles();
        edr.findElement(By.cssSelector(".confirm-terms [data-tooltip]")).click();

        //перемикаємо фокус WebDriver на нщойно відкрити вкладку
        String newWindows = wait.until(anyWindowOtherThan(exitWindows));
        driver.switchTo().window(newWindows);

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertEquals(ukrmailUrl + "/terms_en.html", driver.getCurrentUrl(),
                "Посилання сторінки 'Угоди використання' не відповідає Англійській локалізації.");

        softAssertion.assertEquals("Угода про використання електронної пошти FREEMAIL (mail.ukr.net)", edr.findElement(By.cssSelector(".register > h3")).getText(),
                "Текст заголовку сторінки terms не відповідає Англійській локалізації.");

        softAssertion.assertAll();

        driver.close();
        driver.switchTo().window(parentHandle);
        driver.navigate().refresh();
    }
}