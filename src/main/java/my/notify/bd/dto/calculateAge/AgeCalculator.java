package my.notify.bd.dto.calculateAge;

import my.notify.bd.dto.User;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class AgeCalculator {
    private static Map<String, String> parseMonth() {
        Map<String, String> map = new HashMap<>();

        map.put("Январь", "JANUARY");
        map.put("Февраль","FEBRUARY");
        map.put("Март","MARCH");
        map.put("Апрель","APRIL");
        map.put("Май","MAY");
        map.put("Июнь","JUNE");
        map.put("Июль","JULY");
        map.put("Август","AUGUST");
        map.put("Сентябрь","SEPTEMBER");
        map.put("Октябрь","OCTOBER");
        map.put("Ноябрь","NOVEMBER");
        map.put("Декабрь","DECEMBER");

        return map;
    }

    private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

    public static int getAge(User user) {
        String month = parseMonth().get(user.getMonth());

        LocalDate birthDate = LocalDate.of(user.getYear(), Month.valueOf(month), user.getDay());
        LocalDate currentDate = LocalDate.now();

        return calculateAge(birthDate, currentDate);
    }
}
