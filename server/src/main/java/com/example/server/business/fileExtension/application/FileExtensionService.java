package com.example.server.business.fileExtension.application;

import com.example.server.business.fileExtension.domain.repository.FileExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileExtensionService {

    private final FileExtensionRepository fileExtensionRepository;
}
