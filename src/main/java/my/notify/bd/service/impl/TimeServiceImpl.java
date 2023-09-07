package my.notify.bd.service.impl;

import my.notify.bd.dto.TimeDto;
import my.notify.bd.service.TimeService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@EnableScheduling
public class TimeServiceImpl implements TimeService {
    @Override
    public TimeDto getTimeDto() {
        return new TimeDto(
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                parseMonth().get(Calendar.getInstance(Locale.getDefault()).getTime().toString().substring(4, 7)),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DATE) + "",
                Calendar.getInstance(Locale.getDefault()).getTime().toString().substring(0, 3),
                Calendar.getInstance(Locale.getDefault()).getTime().toString()
        );
    }

    private Map<String, String> parseMonth() {
        Map<String, String> map = new HashMap<>();

        map.put("Jan","Янв");
        map.put("Feb","Фев");
        map.put("Mar","Мар");
        map.put("Apr","Апр");
        map.put("May","Май");
        map.put("Jun","Июн");
        map.put("Jul","Июл");
        map.put("Aug","Авг");
        map.put("Sep","Сен");
        map.put("Oct","Окт");
        map.put("Nov","Ноя");
        map.put("Dec","Дек");

        return map;
    }
}
