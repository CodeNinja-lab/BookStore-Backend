package com.ucad.m2SIR.SenBook.repository;

import com.ucad.m2SIR.SenBook.model.DetailsLivre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsLivreRepository extends JpaRepository<DetailsLivre, Integer> {
    List<DetailsLivre> findAllByLivreId(int livreId);
}
