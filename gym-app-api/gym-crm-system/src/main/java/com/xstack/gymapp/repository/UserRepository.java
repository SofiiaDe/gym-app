package com.xstack.gymapp.repository;

import com.xstack.gymapp.model.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);

  @Query("select u FROM User u where u.isActive = :isActive")
  List<User> findAllByIsActive(boolean isActive);

}
