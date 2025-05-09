package com.ucad.m2SIR.SenBook.repository;

import com.ucad.m2SIR.SenBook.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("SELECT t FROM Token t INNER JOIN t.utilisateur u WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
    List<Token> findValidTokensByUtilisateurId(@Param("id") Integer id);

    Optional<Token> findByToken(String token);
}
