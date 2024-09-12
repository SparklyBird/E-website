package com.ecommerce.website.dao.user;

import com.ecommerce.website.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByLogin(String login);
        void deleteByLogin(String login);
}

