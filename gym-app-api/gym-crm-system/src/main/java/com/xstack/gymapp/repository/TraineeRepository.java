package com.xstack.gymapp.repository;

import com.xstack.gymapp.model.entity.Trainee;
import com.xstack.gymapp.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

  Optional<Trainee> findByUser(User user);
}
