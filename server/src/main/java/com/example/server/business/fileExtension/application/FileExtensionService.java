package com.example.server.business.fileExtension.application;

import com.example.server.business.fileExtension.api.request.CreateExtensionRequest;
import com.example.server.business.fileExtension.api.request.UpdateExtensionRequest;
import com.example.server.business.fileExtension.api.response.ExtensionResponse;
import com.example.server.business.fileExtension.domain.ExtensionValidator;
import com.example.server.business.fileExtension.domain.FileExtension;
import com.example.server.business.fileExtension.domain.repository.ExtensionHistoryRepository;
import com.example.server.business.fileExtension.domain.repository.FileExtensionRepository;
import com.example.server.global.exception.CustomException;
import com.example.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileExtensionService {

    private final FileExtensionRepository fileExtensionRepository;
    private final ExtensionHistoryRepository extensionHistoryRepository;
    private final ExtensionValidator extensionValidator;

    @Transactional
    public void createOrUpdateExtension(CreateExtensionRequest request) {
        // 입력값 유효성 검증
        String extensionName = extensionValidator.validateExtension(request.getExtension());
        // 있으면 호출 아니면 생성
        FileExtension fileExtension = findExtension(extensionName, true);

        // builtin이 아니면 현재개수 확인 + 이미 enabled면 에러반환
        if (!fileExtension.isBuiltIn()) {
            extensionValidator.checkCount();
            checkAlreadyEnabled(fileExtension);
        }
        fileExtension.enable();
        fileExtensionRepository.save(fileExtension);
    }

    @Transactional
    public void updateExtension(UpdateExtensionRequest request) {
        // 입력값 유효성 검증
        String extensionName = extensionValidator.validateExtension(request.getExtension());
        // 있으면 호출 없으면 에러
        FileExtension fileExtension = findExtension(extensionName, false);

        // builtin이 아니고 enable이면 현재개수 확인 + 이미 enabled면 에러반환
        if (!fileExtension.isBuiltIn() && request.isEnabled()) {
            extensionValidator.checkCount();
            checkAlreadyEnabled(fileExtension);
        }
        // 상태 변환
        if (request.isEnabled()) {
            fileExtension.enable();
        } else {
            fileExtension.disable();
        }
    }

    public List<ExtensionResponse> getExtensionList() {
        List<FileExtension> extensionList = fileExtensionRepository.findAllAllowed();

        return extensionList.stream()
                .map(ExtensionResponse::of)
                .toList();
    }

    private void checkAlreadyEnabled(FileExtension fileExtension) {
        if (fileExtension.isEnabled()) {
            throw new CustomException(ErrorCode.INVALID_EXTENSION_STATUS)
                    .addParams("Extension",fileExtension.getExtension());
        }
    }

    private FileExtension findExtension(String ext, boolean isCreate) {
        if (fileExtensionRepository.existsByExtension(ext)) {
            return fileExtensionRepository.findByExtension(ext);
        }
        if (!isCreate) {
            throw new CustomException(ErrorCode.EXTENSION_NOT_FOUND)
                    .addParams("Extension Name", ext);
        }
        return FileExtension.create(ext, false);
    }
}
