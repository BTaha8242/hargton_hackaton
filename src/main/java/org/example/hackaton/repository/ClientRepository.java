package org.example.hackaton.repository;

import org.example.hackaton.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT * FROM client WHERE ref_client = :ref", nativeQuery = true)
    Optional<Client> findByRef(String ref);
}
