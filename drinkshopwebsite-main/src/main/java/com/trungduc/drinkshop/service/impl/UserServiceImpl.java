package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.Role;
import com.trungduc.drinkshop.entity.User;
import com.trungduc.drinkshop.enums.Status;
import com.trungduc.drinkshop.repository.DrinkRepository;
import com.trungduc.drinkshop.service.UserService;
import com.trungduc.drinkshop.repository.UserRepository;
import com.trungduc.drinkshop.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DrinkRepository drinkRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getAllUserOrderByCreatedDate(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable);
    }


    @Override
    public void addDrinkToUser(Long userId, Long DrinkId) {
        User user = userRepository.findById(userId).orElse(null);
        Drink drink = drinkRepository.findById(DrinkId).orElse(null);

        if (user != null && drink != null) {
            user.addFavoriteDrink(drink);
            userRepository.save(user);
        }
    }

    @Override
    public void removeDrinkFromUser(Long userId, Long DrinkId) {
        User user = userRepository.findById(userId).orElse(null);
        Drink drink = drinkRepository.findById(DrinkId).orElse(null);

        if (user != null && drink != null) {
            user.removeFavoriteDrink(drink);
            userRepository.save(user);
        }
    }

    @Override
    public Long countUser() {
        return userRepository.count();
    }

    @Override
    public void saveUserForWebSocket(User user) {
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
    }

    @Override
    public void disconnectUser(User user) {
        var storedUser = userRepository.findById(user.getId()).orElse(null);
        if(storedUser != null){
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    @Override
    public List<User> findConnedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        user.getRoles().add(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

}
