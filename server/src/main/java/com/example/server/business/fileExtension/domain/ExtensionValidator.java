package com.example.server.business.fileExtension.domain;

import com.example.server.business.fileExtension.domain.repository.FileExtensionRepository;
import com.example.server.global.exception.CustomException;
import com.example.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ExtensionValidator {

    private final FileExtensionRepository fileExtensionRepository;
    private final Set<String> builtInSet = new HashSet<>(
            Set.of("bat","cmd","com","cpl","exe","src","js"));

    private static final int MAX_CUSTOM = 200;

    public String validateExtension(String ext) {
        if (ext==null) {
            throw new CustomException(ErrorCode.INVALID_EXTENSION_VALUE)
                    .addParams("extension name",ext);
        }
        String normalized =  ext.replace(" ","")
                .trim()
                .toLowerCase(Locale.ROOT);
        if (normalized.isBlank() || !normalized.matches("[a-z0-9]+")) {
            throw new CustomException(ErrorCode.INVALID_EXTENSION_VALUE)
                    .addParams("extension name",ext);
        }
        return normalized;
    }
    public void checkCount() {
        int currentCount = fileExtensionRepository.
                countByBuiltInFalseAndEnabledFalse();
        if (currentCount > MAX_CUSTOM) {
            throw new CustomException(ErrorCode.EXCEED_EXTENSION_COUNT);
        }
    }
    public boolean isBuiltInSet(String ext) {
        return builtInSet.contains(ext);
    }
}
