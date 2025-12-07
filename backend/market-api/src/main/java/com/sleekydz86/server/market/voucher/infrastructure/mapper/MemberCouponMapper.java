package com.sleekydz86.server.market.voucher.infrastructure.mapper;

import com.sleekydz86.server.market.coupon.domain.MemberCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberCouponMapper {

    void save(MemberCoupon memberCoupon);

    List<MemberCoupon> findAllByMemberId(@Param("memberId") Long memberId);

    void deleteByMemberIdAndCouponIdIn(@Param("memberId") Long memberId, @Param("couponIds") List<Long> couponIds);

    int countMemberIdWithCouponIds(@Param("memberId") Long memberId, @Param("couponIds") List<Long> couponIds);
}




