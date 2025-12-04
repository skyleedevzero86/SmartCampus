package com.sleekydz86.server.market.product.mapper;

import com.sleekydz86.server.market.product.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {

    void save(Product product);

    Optional<Product> findById(@Param("id") Long id);

    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

    void deleteById(@Param("id") Long id);

    List<Product> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
