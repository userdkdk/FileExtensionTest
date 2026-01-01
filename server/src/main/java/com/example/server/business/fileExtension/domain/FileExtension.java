package com.example.server.business.fileExtension.domain;

import com.example.server.global.common.entity.BaseEntity;
import com.example.server.global.exception.CustomException;
import com.example.server.global.exception.code.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "file_extensions",
    uniqueConstraints = {
        @UniqueConstraint(
                name="uk_extension_id",
                columnNames = {"extension"}
        )
    }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileExtension extends BaseEntity {
    private static final int MAX_EXTENSION_LEN = 15;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name="enabled", nullable = false)
    private boolean enabled;

    @Column(name="built_in",nullable = false)
    private boolean builtIn = false;

    private FileExtension(String extension, boolean enabled) {
        this.extension = extension;
        this.enabled = enabled;
    }
    public static FileExtension create(String extension, boolean enableStatus) {
        validateExtension(extension);
        return new FileExtension(extension, enableStatus);
    }

    public void enableBuiltIn() {
        this.enabled = true;
    }

    public void enableNotBuiltIn(ExtensionCounter extensionCounter) {
        if (this.enabled) return;
        extensionCounter.upper();
        this.enabled = true;
    }

    public void disableBuiltIn() {
        this.enabled = false;
    }

    public void disableNotBuiltIn(ExtensionCounter extensionCounter) {
        if (!this.enabled) return;
        extensionCounter.lower();
        this.enabled = false;
    }

    private static void validateExtension(String extension) {
        if (extension.length()>MAX_EXTENSION_LEN) {
            throw new CustomException(ErrorCode.INVALID_EXTENSION_VALUE)
                    .addParams("extension name",extension);
        }
    }
}