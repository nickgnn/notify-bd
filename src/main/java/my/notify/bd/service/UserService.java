package my.notify.bd.service;

import my.notify.bd.dto.User;

public interface UserService {
    String getAllUsers(String chatId);
    void getOneUser(String chatId, Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    String getBirthday();
}
