package com.ecommerce.website.dao.base;

import com.ecommerce.website.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
