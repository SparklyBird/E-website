package com.ecommerce.website.dao.user;

import com.ecommerce.website.model.base.Product;
import com.ecommerce.website.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

