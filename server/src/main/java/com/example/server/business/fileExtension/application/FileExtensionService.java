package com.example.server.business.fileExtension.application;

import com.example.server.business.fileExtension.api.request.CreateExtensionRequest;
import com.example.server.business.fileExtension.api.request.UpdateExtensionRequest;
import com.example.server.business.fileExtension.api.response.ExtensionResponse;
import com.example.server.business.fileExtension.domain.ExtensionCounter;
import com.example.server.business.fileExtension.domain.ExtensionValidator;
import com.example.server.business.fileExtension.domain.FileExtension;
import com.example.server.business.fileExtension.domain.repository.ExtensionCounterRepository;
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
    private final ExtensionValidator extensionValidator;
    private final ExtensionCounterRepository extensionCounterRepository;

    @Transactional
    public void createOrEnableExtension(CreateExtensionRequest request) {
        // 입력값 유효성 검증 및 normalize
        String extensionName = extensionValidator.validateExtension(request.getExtension());

        // 조회
        FileExtension fileExtension = fileExtensionRepository.findByExtension(extensionName);
        ExtensionCounter extensionCounter = extensionCounterRepository.findById(1)
                .orElseThrow(()-> new CustomException(ErrorCode.INTERNAL_DB_ERROR));

        // 조회 후 있으면 enable로 변경, 없으면 생성 후 저장
        if (fileExtension != null) {
            if (fileExtension.isBuiltIn()) {
                fileExtension.enableBuiltIn();
                return;
            }
            fileExtension.enableNotBuiltIn(extensionCounter);
            return;
        }
        fileExtension = FileExtension.create(extensionName, false);
        fileExtension.enableNotBuiltIn(extensionCounter);
        fileExtensionRepository.save(fileExtension);
    }

    @Transactional
    public void disableExtension(UpdateExtensionRequest request) {
        // 입력값 유효성 검증
        String extensionName = extensionValidator.validateExtension(request.getExtension());
        // 있으면 호출 없으면 에러
        FileExtension fileExtension = fileExtensionRepository.findByExtension(extensionName);
        if (fileExtension == null) {
            throw new CustomException(ErrorCode.EXTENSION_NOT_FOUND)
                    .addParams("Extension Name", extensionName);
        }

        if (fileExtension.isBuiltIn()) {
            fileExtension.disableBuiltIn();
            return;
        }

        ExtensionCounter extensionCounter = extensionCounterRepository.findById(1)
                .orElseThrow(()-> new CustomException(ErrorCode.INTERNAL_DB_ERROR));

        fileExtension.disableNotBuiltIn(extensionCounter);
    }

    public List<ExtensionResponse> getExtensionList() {
        List<FileExtension> extensionList = fileExtensionRepository.findAllAllowed();

        return extensionList.stream()
                .map(ExtensionResponse::of)
                .toList();
    }
}
