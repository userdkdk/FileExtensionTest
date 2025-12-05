package com.example.server.business.fileExtension.api;

import com.example.server.business.fileExtension.application.FileExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/extension")
@RequiredArgsConstructor
public class FileExtensionController {

    private final FileExtensionService fileExtensionService;
}
