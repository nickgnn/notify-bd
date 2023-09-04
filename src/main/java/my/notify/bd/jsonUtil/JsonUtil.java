package my.notify.bd.jsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class JsonUtil {
    private static final Logger logger = Logger.getLogger(JsonUtil.class.getName());

    public static void createJson(String chatId) {
        File json = new File("D://" + chatId + ".json");

        try {
            boolean newFile = json.createNewFile();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}