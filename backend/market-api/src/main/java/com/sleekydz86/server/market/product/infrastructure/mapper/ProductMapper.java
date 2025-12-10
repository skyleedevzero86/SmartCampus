package com.sleekydz86.server.market.product.infrastructure.mapper;

import com.sleekydz86.server.market.member.domain.member.dto.ProductByMemberResponse;
import com.sleekydz86.server.market.product.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ProductMapper {

    Optional<Product> findById(@Param("id") Long id);

    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

    List<Product> findAllByCategoryId(@Param("categoryId") Long categoryId);

    void executeProductCUD(Map<String, Object> params);

    List<ProductByMemberResponse> findProductsByMemberId(@Param("memberId") Long memberId);
}
