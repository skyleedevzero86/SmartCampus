package com.sleekydz86.server.market.category.infrastructure;

import com.sleekydz86.server.market.category.domain.Category;
import com.sleekydz86.server.market.category.domain.CategoryRepository;
import com.sleekydz86.server.market.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public Category save(final Category category) {
        categoryMapper.save(category);
        return category;
    }
}
