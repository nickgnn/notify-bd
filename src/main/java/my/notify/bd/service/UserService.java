package my.notify.bd.service;

import my.notify.bd.dto.User;

public interface UserService {
    String getAllUsers(String chatId);
    void getOneUser(String chatId, Integer id);
    void createUser(User user, String chatId);
    void deleteUser(Integer id, String chatId);
    String getBirthday();
}
