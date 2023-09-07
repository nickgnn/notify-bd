package my.notify.bd.service.impl;

import my.notify.bd.dto.User;
import my.notify.bd.jsonUtil.JsonUtil;
import my.notify.bd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {
    private RestTemplate restTemplate;
    private String crudUrl;

    @Autowired
    public UserServiceImpl(RestTemplateBuilder restTemplateBuilder, @Value("${crudUrl}") String crudUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.crudUrl = crudUrl;
    }

    @Override
    public String getAllUsers(String chatId) {
//        List<User> body = restTemplate.exchange(
//                crudUrl + "/api/users/all",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<User>>() {
//                }
//        ).getBody();

        List<User> allUsers = JsonUtil.getAllUsers(chatId);

//        return allUsers.size()!=0 ? concatResult(allUsers) : "У тебя нету дружочков, балбес :)";
        return null;
    }

    @Override
    public void getOneUser(String chatId, Long id) {
        String oneUser = JsonUtil.getOneUser(chatId, id);
    }

    @Override
    public User createUser(User user, String chatId) {
        return JsonUtil.createUser(user, chatId);
    }

    @Override
    public User updateUser(Long id, User user) {
        restTemplate.put(crudUrl + "/api/users" + id, user);

//        return getOneUser(id);

        return null;
    }

    @Override
    public void deleteUser(Long id) {
//        restTemplate.delete(crudUrl + "/api/users/" + id);
    }

    @Override
    public String getBirthday() {
//        String URL = url + "api/seebd/ask";
//        return restTemplate.getForObject(URL, String.class);

        return "";
    }

    private String concatResult(List<User> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : list) {
            stringBuilder
                    .append("{").append(user.getId()).append("} ")
                    .append(" ").append(user.getName())
                    .append(" ").append(user.getYear())
                    .append(", ").append(user.getMonth())
                    .append(" ").append(user.getDay()).append(",")
                    .append(" ").append(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - user.getYear())
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