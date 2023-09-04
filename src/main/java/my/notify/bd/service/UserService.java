package my.notify.bd.service;

import my.notify.bd.dto.User;

public interface UserService {
    void getAllUsers();
    void getOneUser(Long id);
    void createUser(User user);
    void updateUser(Long id, User user);
    void deleteUser(Long id);
    void getBirthday();
}
