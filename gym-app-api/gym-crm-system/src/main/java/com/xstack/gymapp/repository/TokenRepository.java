package com.xstack.gymapp.repository;

import com.xstack.gymapp.model.entity.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByToken(String token);

  @Query(value = """
      SELECT t FROM Token t INNER JOIN User u\s  
      ON t.user.id = u.id\s
      WHERE u.id = :id AND (t.expired = false OR t.revoked = false)  
      """)
  List<Token> findAllValidTokensByUser(Long id);

}
