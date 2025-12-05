package com.example.server.business.fileExtension.domain;

import com.example.server.global.common.entity.BaseEntity;
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

    @Column(name = "extension", nullable = false)
    private String extension;

    @Enumerated(EnumType.STRING)
    @Column(name="enalbe_status", nullable = false)
    private EnableStatus enableStatus;

    @Column(name="built_in",nullable = false)
    private boolean builtIn = false;

    private FileExtension(String extension, EnableStatus enableStatus) {
        this.extension = extension;
        this.enableStatus = enableStatus;
    }
    public static FileExtension create(String extension, EnableStatus enableStatus) {

        return new FileExtension(extension, enableStatus);
    }

    private static void validateExtension(String extension) {

    }
}