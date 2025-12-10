package com.sleekydz86.server.market.member.infrastructure.member;

import com.sleekydz86.server.market.member.domain.member.dto.ProductByMemberResponse;
import com.sleekydz86.server.market.product.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final ProductMapper productMapper;

    public List<ProductByMemberResponse> findProductsByMemberId(final Long memberId) {
        return productMapper.findProductsByMemberId(memberId);
    }
}