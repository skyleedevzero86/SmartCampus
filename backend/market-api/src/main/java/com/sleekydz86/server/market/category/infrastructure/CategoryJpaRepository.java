package com.sleekydz86.server.market.category.infrastructure;

import com.sleekydz86.server.market.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Category save(final Category category);
}
