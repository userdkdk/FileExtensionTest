package com.example.server.business.fileExtension.domain.repository;

import com.example.server.business.fileExtension.domain.FileExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileExtensionRepository extends JpaRepository<FileExtension, Integer> {
    boolean existsByExtension(String ext);

    FileExtension findByExtension(String ext);

    int countByBuiltInFalseAndEnabledFalse();

    @Query("""
        select e
        from FileExtension e
        WHERE e.builtIn = true or e.enabled =true
        ORDER BY e.builtIn DESC,e.extension ASC
        """)
    List<FileExtension> findAllAllowed();
}
