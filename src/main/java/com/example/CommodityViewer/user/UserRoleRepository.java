package com.example.CommodityViewer.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    void save(String user);


    Optional<UserRole> findByName(String name);
}