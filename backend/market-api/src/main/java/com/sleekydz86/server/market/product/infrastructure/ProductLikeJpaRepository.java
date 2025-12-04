package com.sleekydz86.server.market.product.infrastructure;

import com.sleekydz86.server.market.product.domain.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {

    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END FROM ProductLike pl WHERE pl.productId = :productId AND pl.memberId = :memberId")
    boolean existsByProductIdAndMemberId(@Param("productId") Long productId, @Param("memberId") Long memberId);

    void deleteByProductIdAndMemberId(Long productId, Long memberId);
}
