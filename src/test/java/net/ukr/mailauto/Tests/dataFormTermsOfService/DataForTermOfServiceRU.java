package net.ukr.mailauto.Tests.dataFormTermsOfService;

class DataForTermOfServiceRU {

    private String RU = "button:nth-of-type(2) > .header__lang-long-name";

    private String actualTextForTermsOfService = "Регистрация почтового ящика";
    private String messageErrorTextForTermsOfService = "Вибрана не російська мова, тест зупинено";

    private String urlForTermsOfService = "/terms_ru.html";
    private String messageErrorUrlForTermsOfService = "Посилання сторінки 'Угоди використання' не відповідає Російській локалізації.";

    private String actualHeaderForTermsOfService = "Соглашение об использовании электронной почты FREEMAIL (mail.ukr.net)";
    private String messageErrorHeaderForTermsOfService = "Текст заголовку сторінки terms не відповідає Російській локалізації.";
}
