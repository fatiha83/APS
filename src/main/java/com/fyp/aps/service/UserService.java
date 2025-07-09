package com.fyp.aps.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp.aps.model.PasswordResetToken;
import com.fyp.aps.model.User;
import com.fyp.aps.repository.PasswordResetTokenRepository;
import com.fyp.aps.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser (User user){
        userRepository.save(user);
    }

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetToken(User user, String token) {
    PasswordResetToken myToken = new PasswordResetToken();
    myToken.setToken(token);
    myToken.setUser(user);
    myToken.setExpiryDate(LocalDateTime.now().plusHours(1));
    passwordResetTokenRepository.save(myToken);
    }

    public void deleteToken(String token) {
    passwordResetTokenRepository.deleteByToken(token);
    }



}
