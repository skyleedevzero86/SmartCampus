package com.sleekydz86.server.market.product.mapper;

import com.sleekydz86.server.market.product.domain.ProductLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductLikeMapper {

    void save(ProductLike productLike);

    boolean existsByProductIdAndMemberId(@Param("productId") Long productId, @Param("memberId") Long memberId);

    void deleteByProductIdAndMemberId(@Param("productId") Long productId, @Param("memberId") Long memberId);
}
