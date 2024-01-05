package com.example.CommodityViewer.user;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final String DEFAULT_USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        userRoleRepository.deleteById(userId);
    }
    public Page<User> searchUsers(String search, Pageable pageable) {
        return userRepository.findByEmailContainingIgnoreCase(search, pageable);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    @Transactional
    public void registerUserWithDefaultRole(UserRegistrationDto userRegistration) {
        UserRole defaultRole = userRoleRepository.findByName(DEFAULT_USER_ROLE)
                .orElseThrow(() -> new IllegalArgumentException("Domyślna rola użytkownika nie istnieje"));

        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.getRoles().add(defaultRole);
        userRepository.save(user);
    }
    @Transactional
    public void registerAdmin() {
        UserRole defaultRole = userRoleRepository.findByName(ADMIN_ROLE)
                .orElseThrow(() -> new IllegalArgumentException("Domyślna rola użytkownika nie istnieje"));
        User user = new User();
        user.setEmail("admin@example.com");
        user.setPassword(passwordEncoder.encode("adminpass"));
        user.getRoles().add(defaultRole);
        userRepository.save(user);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}