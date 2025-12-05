package com.example.server.business.fileExtension.application;

import com.example.server.business.fileExtension.api.request.CreateExtensionRequest;
import com.example.server.business.fileExtension.api.request.UpdateExtensionRequest;
import com.example.server.business.fileExtension.api.response.ExtensionResponse;
import com.example.server.business.fileExtension.domain.ExtensionValidator;
import com.example.server.business.fileExtension.domain.FileExtension;
import com.example.server.business.fileExtension.domain.repository.ExtensionHistoryRepository;
import com.example.server.business.fileExtension.domain.repository.FileExtensionRepository;
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

    public void createOrUpdateExtension(CreateExtensionRequest request) {
        extensionValidator.checkCount();
        String extensionName = extensionValidator.validateExtension(request.getExtension());

        FileExtension fileExtension = findExtension(extensionName);
        fileExtension.enable();
        // 잠깐만.. builtin 익스텐션이면 추가가 아니잖아
        //그럼 따로 적용되지 이거다시신경써야한다

    }

    public void updateExtension(UpdateExtensionRequest request) {
    }

    public List<ExtensionResponse> getExtensionList() {
        return null;
    }

    private FileExtension findExtension(String ext) {
        if (fileExtensionRepository.existsByExtension(ext)) {
            return fileExtensionRepository.findByExtension(ext);
        }
        return FileExtension.create(ext, true);
    }
}
