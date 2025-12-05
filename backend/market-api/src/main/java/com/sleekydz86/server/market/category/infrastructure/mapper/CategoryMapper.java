package com.sleekydz86.server.market.category.infrastructure.mapper;

import com.sleekydz86.server.market.category.domain.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    void save(Category category);
}