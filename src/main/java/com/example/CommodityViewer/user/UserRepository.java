package com.example.CommodityViewer.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findByEmailContainingIgnoreCase(String search, Pageable pageable);
    List<User> findAll();


    boolean existsByEmail(String email);


}