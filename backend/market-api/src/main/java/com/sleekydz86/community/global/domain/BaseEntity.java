package com.sleekydz86.community.global.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
