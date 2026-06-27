package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }


    @Transactional
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getName() != null && !user.getName().isBlank()) {
            existing.setName(user.getName());
        }

        if (user.getSurename() != null && !user.getSurename().isBlank()) {
            existing.setSurename(user.getSurename());
        }

        if (user.getAge() != null) {
            existing.setAge(user.getAge());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            existing.setEmail(user.getEmail());
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existing.setRoles(user.getRoles());
        }

        String rawPassword = user.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty() && !rawPassword.isBlank()) {
            existing.setPassword(passwordEncoder.encode(rawPassword));
        }
        return userRepository.save(existing);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
