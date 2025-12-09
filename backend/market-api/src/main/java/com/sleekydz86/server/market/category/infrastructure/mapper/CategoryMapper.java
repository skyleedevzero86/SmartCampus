package com.sleekydz86.server.market.category.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CategoryMapper {

    void executeCategoryCUD(Map<String, Object> params);
}