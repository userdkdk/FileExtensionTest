package com.example.server.business.fileExtension.domain;

import com.example.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "extension_history")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtensionHistory extends BaseEntity {

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "file_extension_id", nullable = false)
    private FileExtension fileExtension;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;
}
