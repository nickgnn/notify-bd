package my.notify.bd.jsonUtil;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import my.notify.bd.dto.User;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JsonUtil {
    private static final Gson gson = new Gson();

    private static final Logger logger = Logger.getLogger(JsonUtil.class.getName());

    public static List<User> getAllUsers(String chatId) {
        try {
            List<LinkedTreeMap<String, Object>> mapUsers = getMapUsers(chatId);

            if (mapUsers != null) {
                List<User> userList = transferMapToList(mapUsers);

                logger.log(Level.INFO, "USERS RECEIVED SUCCESSFULLY");

                return userList;

            } else {
                logger.log(Level.WARNING, "ERROR: USERS ARE NULL");
            }
        }catch (NullPointerException er){
            logger.log(Level.WARNING, er.getMessage());
        }

        return null;
    }

    public static void createUser(User user, String chatId) {
        try {
            List<LinkedTreeMap<String, Object>> mapUsers = getMapUsers(chatId);

            if (mapUsers != null & mapUsers.size() != 0) {
                List<User> userList = transferMapToList(mapUsers);

                user.setId(userList.stream().max(Comparator.comparing(User::getId)).get().getId() + 1);
                userList.add(user);

                FileWriter writer = new FileWriter(getFileName(chatId));
                gson.toJson(userList, writer);
                writer.flush();

            } else {
                List<User> userList = new ArrayList();
                user.setId(1);
                userList.add(user);

                FileWriter writer = new FileWriter(getFileName(chatId));
                gson.toJson(userList, writer);
                writer.flush();
            }

            logger.log(Level.INFO, "USER ADDED SUCCESSFULLY");
        }catch (IOException er){
            logger.log(Level.WARNING, er.getMessage());
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
                logger.log(Level.WARNING, "ERROR: USERS ARE NULL");
            }

            logger.log(Level.INFO, "USER REMOVED SUCCESSFULLY");
        }catch (IOException er){
            logger.log(Level.WARNING, er.getMessage());
        }
    }

    public static void createJson(String chatId) {
        File json = new File(getFileName(chatId));

        try {
            boolean newFile = json.createNewFile();

            logger.log(Level.INFO, "Created file is: " + String.valueOf(newFile).toUpperCase());
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
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
            logger.log(Level.WARNING, e.getMessage() + " " + e.getCause());
        }

        return null;
    }
}