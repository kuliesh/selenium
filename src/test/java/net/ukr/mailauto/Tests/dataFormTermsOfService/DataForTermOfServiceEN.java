package net.ukr.mailauto.Tests.dataFormTermsOfService;

class DataForTermOfServiceEN {

    private String EN = "button:nth-of-type(3) > .header__lang-long-name";

    private String actualTextForTermsOfService = "Create Your @UKR.NET Mailbox";
    private String messageErrorTextForTermsOfService = "Вибрана не англійська мова, тест зупинено";

    private String urlForTermsOfService = "/terms_en.html";
    private String messageErrorUrlForTermsOfService = "Посилання сторінки 'Угоди використання' не відповідає Англійській локалізації.";

    private String actualHeaderForTermsOfService = "Угода про використання електронної пошти FREEMAIL (mail.ukr.net)";
    private String messageErrorHeaderForTermsOfService = "Текст заголовку сторінки terms не відповідає Англійській локалізації.";
}
