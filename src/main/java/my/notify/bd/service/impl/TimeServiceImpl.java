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

        map.put("Jan","Январь");
        map.put("Feb","Февраль");
        map.put("Mar","Март");
        map.put("Apr","Апрель");
        map.put("May","Май");
        map.put("Jun","Июнь");
        map.put("Jul","Июль");
        map.put("Aug","Август");
        map.put("Sep","Сентябрь");
        map.put("Oct","Октябрь");
        map.put("Nov","Ноябрь");
        map.put("Dec","Декабрь");

        return map;
    }
}
