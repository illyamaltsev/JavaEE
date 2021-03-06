package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer>, UserRepositoryCustom {

    Optional<UserEntity> findByEmail(String email);

}
