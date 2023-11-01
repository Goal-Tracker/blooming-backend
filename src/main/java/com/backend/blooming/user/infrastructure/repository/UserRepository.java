package com.backend.blooming.user.infrastructure.repository;

import com.backend.blooming.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
