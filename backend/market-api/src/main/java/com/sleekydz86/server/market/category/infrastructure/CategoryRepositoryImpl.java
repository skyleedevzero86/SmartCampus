package com.sleekydz86.server.market.category.infrastructure;

import com.sleekydz86.server.market.category.domain.Category;
import com.sleekydz86.server.market.category.domain.CategoryRepository;
import com.sleekydz86.server.market.category.infrastructure.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public Category save(final Category category) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("name", category.getName().name());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        categoryMapper.executeCategoryCUD(params);
        return category;
    }
}
