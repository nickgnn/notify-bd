package my.notify.bd.jsonUtil;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import my.notify.bd.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class.getName());

    public static List<User> getAllUsers(String chatId) {
        try {
            List<LinkedTreeMap<String, Object>> mapUsers = getMapUsers(chatId);

            if (mapUsers != null) {
                List<User> userList = transferMapToList(mapUsers);

                LOGGER.info("USERS RECEIVED SUCCESSFULLY");

                return userList;

            } else {
                LOGGER.warn("ERROR: USERS ARE NULL");

                return Collections.emptyList();
            }
        }catch (NullPointerException er){
            LOGGER.error(er.getMessage());
        }
        return null;
    }

    private static void addFirstUser(User user, String chatId) throws IOException {
        List<User> userList = new ArrayList();
        user.setId(1);
        userList.add(user);

        FileWriter writer = new FileWriter(getFileName(chatId));
        gson.toJson(userList, writer);
        writer.flush();
    }

    public static void createUser(User user, String chatId) {
        try {
            List<LinkedTreeMap<String, Object>> mapUsers = getMapUsers(chatId);

            if (mapUsers == null) {
                addFirstUser(user, chatId);
            }

            if (mapUsers.size() == 0) {
                addFirstUser(user, chatId);
            } else {
                List<User> userList = transferMapToList(mapUsers);

                user.setId(userList.stream().max(Comparator.comparing(User::getId)).get().getId() + 1);
                userList.add(user);

                FileWriter writer = new FileWriter(getFileName(chatId));
                gson.toJson(userList, writer);
                writer.flush();
            }

            LOGGER.info("USER ADDED SUCCESSFULLY");
        }catch (IOException | NullPointerException er){
            LOGGER.error(er.getMessage());
        }
    }

    public static void deleteUser(Integer id, String chatId) {
        try {
            List<LinkedTreeMap<String, Object>> mapUsers = getMapUsers(chatId);

            if (mapUsers != null) {
                List<User> userList = transferMapToList(mapUsers);

                userList.removeIf(u -> id.equals(u.getId()));

                FileWriter writer = new FileWriter(getFileName(chatId));
                gson.toJson(userList, writer);
                writer.flush();

            } else {
                LOGGER.warn("ERROR: USERS ARE NULL");
            }

            LOGGER.info("USER REMOVED SUCCESSFULLY");
        }catch (IOException er){
            LOGGER.error(er.getMessage());
        }
    }

    public static void createJson(String chatId) {
        File json = new File(getFileName(chatId));

        try {
            boolean newFile = json.createNewFile();

            LOGGER.info("Created file is: " + String.valueOf(newFile).toUpperCase());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static List<User> transferMapToList(List<LinkedTreeMap<String, Object>> users) {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            LinkedTreeMap<String, Object> stringObjectLinkedTreeMap = users.get(i);

            userList.add(new User(
                    ((Double) stringObjectLinkedTreeMap.get("id")).intValue(),
                    (String) stringObjectLinkedTreeMap.get("name"),
                    ((Double) stringObjectLinkedTreeMap.get("year")).intValue(),
                    (String) stringObjectLinkedTreeMap.get("month"),
                    ((Double) stringObjectLinkedTreeMap.get("day")).intValue(),
                    ((Double) stringObjectLinkedTreeMap.get("age")).intValue()));
        }

        return userList;
    }

    private static String getFileName(String chatId) {
        return "D://" + chatId + ".json";
//        return "/home/gnn/" + chatId + ".json";
    }

    private static List<LinkedTreeMap<String, Object>> getMapUsers(String chatId) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getFileName(chatId)));
            List<LinkedTreeMap<String, Object>> users = gson.fromJson(reader, List.class);

            reader.close();

            return users;
        } catch (IOException e) {
            LOGGER.error(e.getMessage() + " " + e.getCause());
        }

        return null;
    }
}