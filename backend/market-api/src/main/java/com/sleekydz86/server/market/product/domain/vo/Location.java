package com.sleekydz86.server.market.product.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Location {

    BUILDING_THREE("동사무소"),
    BUILDING_FIVE("경로당"),
    BUILDING_LIBRARY("편의점"),
    BUILDING_CENTER("보건소"),
    NEAR_MJU("놀이공원");

    private final String content;
}
