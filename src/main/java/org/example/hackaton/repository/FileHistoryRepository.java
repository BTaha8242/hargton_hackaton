package org.example.hackaton.repository;

import org.example.hackaton.entity.FileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileHistoryRepository extends JpaRepository<FileHistory, Long> {

    @Query(value = "SELECT * FROM t_file_history WHERE file_name = :fileName", nativeQuery = true)
    Optional<FileHistory> findByFileName(String fileName);
}
