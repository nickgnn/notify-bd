package my.notify.bd.service.impl;

import my.notify.bd.dto.TimeDto;
import my.notify.bd.dto.User;
import my.notify.bd.dto.calculateAge.AgeCalculator;
import my.notify.bd.jsonUtil.JsonUtil;
import my.notify.bd.service.TimeService;
import my.notify.bd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    private final TimeService timeService;

    @Autowired
    public UserServiceImpl(TimeService timeService) {
        this.timeService = timeService;
    }

    @Override
    public String getAllUsers(String chatId) {
        List<User> allUsers = JsonUtil.getAllUsers(chatId);

        String result = allUsers.size() != 0 ? concatResult(allUsers) : "У тебя нету дружочков, балбес :)";

        LOGGER.info(result);

        return result;
    }

    @Override
    public void createUser(User user, Long chatId) {
        JsonUtil.createUser(user, String.valueOf(chatId));
    }

    @Override
    public void deleteUser(Integer id, Long chatId) {
        JsonUtil.deleteUser(id, String.valueOf(chatId));
    }

    @Override
    public String getBirthday(Long chatId) {
        List<User> allUsers = JsonUtil.getAllUsers(String.valueOf(chatId));

        String birthDay = allUsers.size() != 0 ? isBirthDay(allUsers, timeService.getTimeDto(), String.valueOf(chatId)) : "У тебя нету дружочков, балбес :)";

        LOGGER.info(birthDay);

        return birthDay;
    }

    private String isBirthDay(Iterable<User> users, TimeDto timeDto, String chatId) {
        Iterator<User> iterator = users.iterator();

        //Отсев по месяцам
        while (iterator.hasNext()) {
            if (!iterator.next().getMonth().equals(timeDto.getMonth())) {
                iterator.remove();
            }
        }

        //Отсев по числу месяца
        iterator = users.iterator();
        while (iterator.hasNext()) {
            if (!String.valueOf(iterator.next().getDay()).equals(timeDto.getDayOfMonth())) {
                iterator.remove();
            }
        }

        //Преобразование в ArrayList
        ArrayList<User> list = (ArrayList<User>)users;

        //Проверка на наличие ДР (если лист пустой, то сегодня ни у кого нет ДР)
        if (list.isEmpty()) {
            return "Сегодня ни у кого нет ДР :(";
        } else {
            return concatResult(list, chatId);
        }
    }

    private String concatResult(ArrayList<User> list, String chatId) {
        StringBuilder stringBuilder = new StringBuilder("Сегодня ДР у:" + '\n');

        for (User user : list) {
            stringBuilder
                    .append("{").append(user.getId()).append("}")
                    .append(" ").append(user.getName())
                    .append(" ").append(user.getYear())
                    .append(" ").append(user.getMonth())
                    .append(" ").append(user.getDay())
                    .append(", исполняется")
                    .append(" ").append(AgeCalculator.getAge(user)).append(" годиков;")
                    .append("\n");

            JsonUtil.updateUserAge(user, chatId);
        }

        return stringBuilder.toString();
    }

    private String concatResult(List<User> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : list) {
            stringBuilder
                    .append("{").append(user.getId()).append("} ")
                    .append(" ").append(user.getName() + ": ")
                    .append(" ").append(user.getYear())
                    .append(", ").append(user.getMonth())
                    .append(" ").append(user.getDay()).append(",")
                    .append(" ").append(user.getAge())
                    .append("\n");
        }

        return normalizeMessage(stringBuilder.toString());
    }

    private String normalizeMessage(String message) {
        String[] units = message.split("\n");
        StringBuilder result = new StringBuilder();

        Arrays.stream(units)
                .forEach(unit -> {
                    if (unit.endsWith("1")) {
                        result.append(unit + " годик\n");
                    } else if (unit.endsWith("2")) {
                        result.append(unit + " годика\n");
                    } else if (unit.endsWith("3")) {
                        result.append(unit + " годика\n");
                    } else if (unit.endsWith("4")) {
                        result.append(unit + " годика\n");
                    } else {
                        result.append(unit + " годиков\n");
                    }
                });

        return result.toString();
    }
}