package my.notify.bd.service;

import my.notify.bd.dto.User;

public interface UserService {
    String getAllUsers(String chatId);
    void createUser(User user, Long chatId);
    void deleteUser(Integer id, Long chatId);
    String getBirthday(Long chatId);
}
