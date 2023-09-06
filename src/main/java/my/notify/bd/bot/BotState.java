package my.notify.bd.bot;

import my.notify.bd.jsonUtil.JsonUtil;
import my.notify.bd.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.logging.Logger;

@Component
public enum BotState {
    Start() {
        @Override
        public void enter(Bot bot) {
            bot.sendMessage("Привет");
        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService) {
            SendMessage sm = new SendMessage();
            Long chatId = bot.getChatId();

            SendMessage sendMessageKeyBoard = bot.sendKeyBoardMessage(chatId);
            bot.sendMessage(sendMessageKeyBoard);

            String command = update.getMessage().getText();
            bot.getLogger().info(command);

            String[] seq = command.split(" ");

            switch (seq[0]) {
                case "Узнать":
                    userService.getBirthday();
                    break;
                case "Показать":
                    sm.setText(userService.getAllUsers(String.valueOf(bot.getChatId())));
                    bot.sendMessage(sm);
                    break;
                case "Добавить":
                    bot.createNewUser();
                    break;
                case "Удолить":
                    bot.setIdForDelete();
                    break;
                case "/start":
                    sm.setText("Твой chatID - " + chatId);
                    bot.sendMessage(sm);
                    break;
                default:
                    System.out.println("Oooops, something wrong !");
            }
        }

        @Override
        public void setUser(Bot bot, String data) {

        }
        @Override
        public BotState nextState() {
            return FillName;
        }
    },
    FillName() {
        @Override
        public void enter(Bot bot) {
            bot.sendMessage("Create");
        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService) {
            bot.createNewUser();
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillName(data);
        }

        @Override
        public BotState nextState() {
            return FillYear;
        }
    },
    FillYear() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService) {
            bot.createNewUser();
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillYear(data);
        }

        @Override
        public BotState nextState() {
            return FillMonth;
        }
    },
    FillMonth() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService) {
            bot.createNewUser();
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillMonth(data);
        }

        @Override
        public BotState nextState() {
            return FillDay;
        }
    },
    FillDay() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService) {
            bot.createUser(bot.fillAge(bot.createNewUser()));
            bot.setBotState(bot.getBotState().nextState());
            bot.sendMessage("Success! Новый дружок успешно добавлен в базу :)");
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillDay(data);
        }

        @Override
        public BotState nextState() {
            return Start;
        }
    },
    Delete() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService) {
            bot.deleteUser(update.getMessage().getText());
            bot.sendMessage("Success! Пользователь успешно удалён.");
        }

        @Override
        public void setUser(Bot bot, String data) {

        }

        @Override
        public BotState nextState() {
            return null;
        }
    };

    private static BotState[] states;
    private final Logger logger = Logger.getLogger(BotState.class.getName());
    public static BotState getInitialState(Long chatId) {
        JsonUtil.createJson(String.valueOf(chatId));

        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    public abstract void enter(Bot bot);
    public abstract void handleInput(Bot bot, BotState botState, Update update, UserService userService);
    public abstract void setUser(Bot bot, String data);
    public abstract BotState nextState();
}