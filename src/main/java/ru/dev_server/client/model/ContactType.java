package ru.dev_server.client.model;

import java.util.regex.Pattern;

/**.*/
public  enum ContactType {

    MOBILE("+код_страны(код_оператора)номер абонента","+7(926)555-55-55","^\\+\\d{1,3}\\s?\\(\\d{1,5}\\)\\s?\\d{3}(-?\\d{1,3}){1,3}$"),
    PHONE("Пример: +7(495)555-55-55","+7(926)555-55-55","^\\+\\d{1,3}\\s?\\(\\d{1,5}\\)\\s?\\d{3}(-?\\d{1,3}){1,3}$"),
    FAX("Пример: +7(495)555-55-55","+7(926)555-55-55","^\\+\\d{1,3}\\s?\\(\\d{1,5}\\)\\s?\\d{3}(-?\\d{1,3}){1,3}$"),
    EMAIL("login@server","info@simplex-crm.ru",".+\\@.+\\..+"),
    ICQ("цифровой номер","1234567890","\\p{Digit}{1,12}"),
    OTHER("любая строка","введите строку",".{1,256}");

    private String formatMsg;
    private String example;
    private Pattern pattern;

    private ContactType(String formatMsg,String example, String regexp) {
        this.formatMsg = formatMsg;
        this.example=example;
        this.pattern =Pattern.compile(regexp);
    }

    public String getFormatMsg(){
        return formatMsg;
    }

    public String getExample() {
        return example;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
