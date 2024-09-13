package com.ecommerce.website.dao.user;

import com.ecommerce.website.model.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Find UserProfile by User ID (if needed)
    Optional<UserProfile> findByUserId(Long userId);
}
