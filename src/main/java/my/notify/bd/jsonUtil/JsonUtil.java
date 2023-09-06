package my.notify.bd.jsonUtil;

import com.google.gson.Gson;
import my.notify.bd.dto.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonUtil {
    private static final Gson gson = new Gson();

    private static final Logger logger = Logger.getLogger(JsonUtil.class.getName());

    public static String getOneUser(String chatId, Long id) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getFileName(chatId)));
            User user = gson.fromJson(reader, User.class);
            logger.log(Level.INFO, "USER RECEIVED SUCCESSFULLY");
        }catch (IOException er){
            logger.log(Level.WARNING, er.getMessage());
        }

        return "";
    }

    public static String getAllUsers(String chatId) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getFileName(chatId)));
            User user = gson.fromJson(reader, User.class);

            if (user == null) {
                logger.log(Level.WARNING, "ERROR: USERS ARE NULL");
            } else {
                logger.log(Level.INFO, "USERS RECEIVED SUCCESSFULLY");
            }

            return user.toString();
        }catch (IOException | NullPointerException er){
            logger.log(Level.WARNING, er.getMessage());
        }

        return "";
    }

    public static void createJson(String chatId) {
        File json = new File(getFileName(chatId));

        try {
            boolean newFile = json.createNewFile();

            logger.log(Level.INFO, "Created file is: " + newFile);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private static String getFileName(String chatId) {
        return "D://" + chatId + ".json";
//        return "/home/gnn/" + chatId + ".json";
    }
}