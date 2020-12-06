package ex2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private enum DateType {
        WEEKDAY, WEEKEND, INVALID
    }

    private static final Map<DateType, String> DATE_TYPE_STRING_MAP = Map.of(
            DateType.WEEKDAY, "будний день",
            DateType.WEEKEND, "выходной день",
            DateType.INVALID, "неверный формат даты");

    private static final String DATE_PATTERN = "dd-MM-yyyy";


    private static DateType getDateType(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
            return localDate.getDayOfWeek().getValue() < 6 ? DateType.WEEKDAY : DateType.WEEKEND;
        } catch (DateTimeParseException e) {
            return DateType.INVALID;
        }
    }

    public static void main(String[] args) {
        System.out.println("Вводите дату (q - выход)");
        Scanner scanner = new Scanner(System.in);
        String date;
        while (!(date = scanner.nextLine()).equals("q")) {
            System.out.println(DATE_TYPE_STRING_MAP.get(getDateType(date)));
        }
    }
}
