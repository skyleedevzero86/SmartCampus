package com.sleekydz86.server.market.coupon.infrastructure;

import com.sleekydz86.server.market.coupon.domain.MemberCoupon;
import com.sleekydz86.server.market.coupon.domain.MemberCouponRepository;
import com.sleekydz86.server.market.coupon.infrastructure.mapper.MemberCouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class MemberCouponRepositoryImpl implements MemberCouponRepository {

    private final MemberCouponMapper memberCouponMapper;
    private final MemberCouponJdbcRepository memberCouponJdbcRepository;

    @Override
    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        return memberCouponMapper.findAllByMemberId(memberId);
    }

    @Override
    public MemberCoupon save(final MemberCoupon memberCoupon) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("memberId", memberCoupon.getMemberId());
        params.put("couponId", memberCoupon.getCouponId());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        memberCouponMapper.executeMemberCouponCUD(params);
        return memberCoupon;
    }

    @Override
    public void deleteByMemberIdAndCouponIdIn(final Long memberId, final List<Long> couponIds) {
        memberCouponMapper.deleteByMemberIdAndCouponIdIn(memberId, couponIds);
    }

    @Override
    public int countMemberIdWithCouponIds(final Long memberId, final List<Long> couponIds) {
        return memberCouponMapper.countMemberIdWithCouponIds(memberId, couponIds);
    }

    @Override
    public void insertBulk(final List<MemberCoupon> memberCoupons) {
        memberCouponJdbcRepository.insertBulk(memberCoupons);
    }
}
