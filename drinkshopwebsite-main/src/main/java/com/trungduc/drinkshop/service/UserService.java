package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> getAllUserOrderByCreatedDate(Pageable pageable);

    User getUserById(Long userId);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(Long userId);

    boolean registerUser(User user);

    void deleteUserById(Long id);

    void saveUser(User user);

    void addDrinkToUser(Long userId, Long DrinkId);

    void removeDrinkFromUser(Long userId, Long DrinkId);

    Long countUser();

    void saveUserForWebSocket(User user);

    void disconnectUser(User user);

    List<User> findConnedUsers();

}
