package com.xstack.gymapp.repository;

import com.xstack.gymapp.model.entity.Trainer;
import com.xstack.gymapp.model.entity.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

  Optional<Trainer> findByUser(User user);
  List<Trainer> findAllByUserIn(List<User> users);

  @Query("select t from Trainer t where t.user.isActive = :isActive")
  List<Trainer> findAllByUserIsActive(boolean isActive);

}
