package com.sleekydz86.server.market.product.infrastructure.mapper;

import com.sleekydz86.server.market.product.domain.ProductLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ProductLikeMapper {

    boolean existsByProductIdAndMemberId(@Param("productId") Long productId, @Param("memberId") Long memberId);

    void executeProductLikeCUD(Map<String, Object> params);
}
