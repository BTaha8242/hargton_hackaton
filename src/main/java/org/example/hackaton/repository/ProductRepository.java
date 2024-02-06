package org.example.hackaton.repository;

import org.example.hackaton.entity.Client;
import org.example.hackaton.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
