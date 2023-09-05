package my.notify.bd.jsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class JsonUtil {

    private static final Logger logger = Logger.getLogger(JsonUtil.class.getName());

    public static void createJson(String chatId) {
        File json = new File(getFileName(chatId));

        try {
            boolean newFile = json.createNewFile();

            logger.info("Created file is: " + newFile);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private static String getFileName(String chatId) {
        return "D://" + chatId + ".json";
//        return "/home/gnn/" + chatId + ".json";
    }
}