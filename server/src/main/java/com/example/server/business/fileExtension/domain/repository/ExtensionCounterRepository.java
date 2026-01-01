package com.example.server.business.fileExtension.domain.repository;

import com.example.server.business.fileExtension.domain.ExtensionCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtensionCounterRepository extends JpaRepository<ExtensionCounter, Integer> {
}
