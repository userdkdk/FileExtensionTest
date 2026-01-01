package com.example.server.business.fileExtension.domain;

import com.example.server.global.common.entity.BaseEntity;
import com.example.server.global.exception.CustomException;
import com.example.server.global.exception.code.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name ="extension_counter")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtensionCounter extends BaseEntity {
    private static final int MAX_CUSTOM = 200;

    @Column(name = "extension_counts", nullable = false)
    private Integer extension_counts;

    public void upper() {
        if (this.extension_counts >= MAX_CUSTOM) {
            throw new CustomException(ErrorCode.EXCEED_EXTENSION_COUNT);
        }
        this.extension_counts++;
    }

    public void lower() {
        if (this.extension_counts <= 0) {
            throw new CustomException(ErrorCode.ERROR_EXTENSION_COUNT);
        }
        this.extension_counts--;
    }
}
