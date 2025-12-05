package com.example.server.business.fileExtension.api.request;

import com.example.server.business.fileExtension.domain.FileExtension;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateExtensionRequest {

    @NotBlank
    private final String extension;
}
