package com.sleekydz86.server.market.coupon.infrastructure.mapper;

import com.sleekydz86.server.market.coupon.domain.MemberCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberCouponMapper {

    List<MemberCoupon> findAllByMemberId(@Param("memberId") Long memberId);

    void deleteByMemberIdAndCouponIdIn(@Param("memberId") Long memberId, @Param("couponIds") List<Long> couponIds);

    int countMemberIdWithCouponIds(@Param("memberId") Long memberId, @Param("couponIds") List<Long> couponIds);

    void executeMemberCouponCUD(Map<String, Object> params);
}




