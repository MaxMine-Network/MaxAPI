package net.maxmine.api.common.utility;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@SuppressWarnings("all")
public enum FormatingStringUtils {

    YEARS(" год", " года", " лет"),
    MONTHS(" месяц", " месяца", " месяцев"),
    WEEK(" неделя", " недели", " недель"),
    DAYS(" день", " дня", " дней"),
    HOURS(" час", " часа", " часов"),
    MINUTES(" минута", " минуты", " минут"),
    SECONDS(" секунда", " секунд", " секунд"),
    PLAYERS(" игрок"," игрока", " игроков"),
    MONEY(" монета", " монет", " монет");

    String one;
    String two;
    String three;

    FormatingStringUtils(String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }
}

