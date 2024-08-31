package my.notify.bd.bot;


import my.notify.bd.dto.User;
import my.notify.bd.dto.calculateAge.AgeCalculator;
import my.notify.bd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@PropertySource("classpath:telegram.properties")
public class Bot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class.getName());
    private User user = new User();
    private Long chatId;
    private BotState botState;

    private final UserService userService;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Autowired
    public Bot(UserService userService) {
        this.userService = userService;
        this.botState = BotState.getInitialState(getChatId());
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.chatId = update.getMessage().getChatId();

        // нагенерил случайный UUID, который просто заполняет строку, если строка переназначается, то значит кто-то пытался открыть бота
        String anotherUserInfo = "62a3e8776ac449ac8d9a1af3940012a1";

        if (chatId != 308464656) {
            Long chatIdOther = update.getMessage().getChatId();
            anotherUserInfo = "Кто-то с chatId " + chatIdOther + " открыл бота";
            LOGGER.info(anotherUserInfo);
            this.chatId = 308464656L;
        }

        if(update.hasMessage() && update.getMessage().hasText()) {

            if (update.getMessage().getText().equals("/start")) {
                this.botState = BotState.getInitialState(getChatId());

                this.user = new User();

                if (anotherUserInfo.contains("62a3e8776ac449ac8d9a1af3940012a1")) {
                    botState.enter(this);
                } else {
                    botState.enter(anotherUserInfo,this);
                }

                botState.handleInput(this, botState, update, userService);

            } else {
                if (botState.equals(BotState.Start)) {
                    botState.handleInput(this, botState, update, userService);
                    return;
                }
                if (botState.equals(BotState.FillName)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService);
                    return;
                }
                if (botState.equals(BotState.FillYear)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService);
                    return;
                }
                if (botState.equals(BotState.FillMonth)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService);
                    return;
                }
                if (botState.equals(BotState.FillDay)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService);

                    this.user = new User();

                    return;
                }
                if (botState.equals(BotState.Delete)) {
                    botState.handleInput(this, botState, update, userService);
                    return;
                }
            }
        }
    }

    public User createNewUser() {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);

        try {
            if (user.getName() == null) {

                sm.setText("Введите имя:");
                this.botState = botState.nextState();
                execute(sm);

            } else if (user.getYear() == null) {

                sm.setText("Введите год:");
                this.botState = botState.nextState();
                execute(sm);

            } else if (user.getMonth() == null) {

                sm.setText("Введите месяц:");
                this.botState = botState.nextState();
                execute(sm);

            } else if (user.getDay() == null) {

                sm.setText("Введите число:");
                this.botState = botState.nextState();
                execute(sm);

            }
        } catch (TelegramApiException e) {
            LOGGER.error(e.getMessage() + " : " + e.getCause());
        }

        return user;
    }

    public User fillName(String data) {
        if (!data.equals("")) {
            this.user.setName(data);
        }

        return this.user;
    }

    public User fillYear(String data) {
        if (!data.equals("")) {
            this.user.setYear(Integer.valueOf(data));
        }

        return this.user;
    }

    public User fillMonth(String data) {
        if (!data.equals("")) {
            this.user.setMonth(data);
        }

        return this.user;
    }

    public User fillDay(String data) {
        if (!data.equals("")) {
            this.user.setDay(Integer.valueOf(data));
        }

        return this.user;
    }

    public User fillAge(User user) {
        this.user.setAge(AgeCalculator.getAge(user));

        return this.user;
    }

    public void createUser(User user, Long chatId) {
        userService.createUser(user, chatId);
    }

    public void setIdForDelete() {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);

        sm.setText("Введите ID бывшего \"друга\":");
        this.botState = BotState.byId(5);

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            LOGGER.error(e.getMessage() + " : " + e.getCause());
        }
    }

    public void deleteUser(String id, Long chatId) {
        userService.deleteUser(Integer.valueOf(id), chatId);
        this.botState = BotState.byId(0);
    }

    public void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        LOGGER.info("CHAT_ID IS: " + this.getChatId());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(e.getMessage() + " : " + e.getCause());
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        sendMessage.setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(e.getMessage() + " : " + e.getCause());
        }
    }

//    @Scheduled(cron = "15 13 12 * * *", zone = "Europe/Moscow")
    @Scheduled(cron = "0 0 10,18 * * *", zone = "Europe/Moscow")
    public void notifyBD() {
        String birthday = userService.getBirthday(getChatId());

        if (!birthday.contains(":(")) {
            sendMessage(birthday);
        }

        LOGGER.info("NOTIFY SENT: " + birthday);
    }

    public SendMessage sendKeyBoardMessage(Long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton startButton = new KeyboardButton();
        KeyboardButton findOutButton = new KeyboardButton();
        KeyboardButton showButton = new KeyboardButton();
        KeyboardButton addButton = new KeyboardButton();
        KeyboardButton deleteButton = new KeyboardButton();

        startButton.setText("/start");
        findOutButton.setText("Узнать у кого сегодня ДР");
        showButton.setText("Показать список всех друзей");
        addButton.setText("Добавить новый ДР");
        deleteButton.setText("Удолить бывшего \"друга\"");

        KeyboardRow keyboardButtonsRow1 = new KeyboardRow();
        KeyboardRow keyboardButtonsRow2 = new KeyboardRow();

        keyboardButtonsRow1.add(startButton);
        keyboardButtonsRow1.add(findOutButton);
        keyboardButtonsRow1.add(showButton);
        keyboardButtonsRow2.add(addButton);
        keyboardButtonsRow2.add(deleteButton);

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        keyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Старт бота ДР-ов...");
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    public Long getChatId() {
        return chatId;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }
}