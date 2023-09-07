package my.notify.bd.service;

import my.notify.bd.dto.User;

public interface UserService {
    String getAllUsers(String chatId);
    void getOneUser(String chatId, Integer id);
    User createUser(User user, String chatId);
    User updateUser(Integer id, User user);
    void deleteUser(Integer id, String chatId);
    String getBirthday();
}
