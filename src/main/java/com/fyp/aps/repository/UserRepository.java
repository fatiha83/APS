package com.fyp.aps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fyp.aps.model.User;

public interface UserRepository extends JpaRepository <User, Long>{
    User findByEmail (String email);
}

