package net.ukr.mailauto.Tests.dataFormTermsOfService;

public class DataForTermOfServiceUA {

    private  String UA = "button:nth-of-type(1) > .header__lang-long-name";

    private String actualTextForTermsOfService = "Реєстрація поштової скриньки";
    private String messageErrorTextForTermsOfService = "Вибрана не Українська мова, тест зупинено.";

    private String urlForTermsOfService = "/terms_uk.html";
    private String messageErrorUrlForTermsOfService = "Посилання сторінки 'Угоди використання' не відповідає Українській локалізації.";

    private String actualHeaderForTermsOfService = "Угода про використання електронної пошти FREEMAIL (mail.ukr.net)";
    private String messageErrorHeaderForTermsOfService = "Текст заголовку сторінки terms не відповідає Українській локалізації.";
}
