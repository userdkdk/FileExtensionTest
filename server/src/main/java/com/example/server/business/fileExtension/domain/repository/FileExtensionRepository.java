package com.example.server.business.fileExtension.domain.repository;

import com.example.server.business.fileExtension.domain.FileExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileExtensionRepository extends JpaRepository<FileExtension, Integer> {
}
