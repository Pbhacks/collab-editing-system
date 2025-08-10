package com.collab.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.collab.user.model.User;
import com.collab.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User user) {
        return repo.save(user);
    }

    public Optional<User> login(String username, String password) {
        return repo.findByUsername(username)
                   .filter(u -> u.getPassword().equals(password));
    }

    public User updateProfile(Long id, User updated) {
        User user = repo.findById(id).orElseThrow();
        user.setEmail(updated.getEmail());
        user.setPassword(updated.getPassword());
        return repo.save(user);
    }
}
