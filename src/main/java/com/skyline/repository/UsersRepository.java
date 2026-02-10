package com.skyline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skyline.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    boolean existsByEmail(String email);
}
