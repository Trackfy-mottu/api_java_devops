package com.example.challenge_mottu_java.service;

import com.example.challenge_mottu_java.dto.UserDTO;
import com.example.challenge_mottu_java.model.User;
import com.example.challenge_mottu_java.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getCourt().courtToDTO(), user.getRole());
    }

    public User createUser(User user) {
            User existingUser = userRepository.findByUsernameIgnoreCase(user.getUsername()).orElse(null);
            if (existingUser != null) throw new RuntimeException("Email ou senha inválidos");
            userRepository.save(user);
            return user;
    }

    public UserDTO updateUser(String username, User newUser) {
        User oldUser = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        newUser.setId(oldUser.getId());
        userRepository.save(newUser);
        return new UserDTO(newUser.getId(), newUser.getUsername(), newUser.getName(), newUser.getCourt().courtToDTO(), newUser.getRole());
    }

    public void deleteUser(Long userId ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        userRepository.delete(user);
    }
}
