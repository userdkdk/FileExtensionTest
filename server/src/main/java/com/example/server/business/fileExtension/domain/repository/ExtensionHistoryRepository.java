package com.example.server.business.fileExtension.domain.repository;

import com.example.server.business.fileExtension.domain.ExtensionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtensionHistoryRepository extends JpaRepository<ExtensionHistory, Integer> {
}
