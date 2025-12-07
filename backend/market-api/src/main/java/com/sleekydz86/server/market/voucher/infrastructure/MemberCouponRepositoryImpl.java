package com.sleekydz86.server.market.voucher.infrastructure;

import com.sleekydz86.server.market.coupon.domain.MemberCoupon;
import com.sleekydz86.server.market.coupon.domain.MemberCouponRepository;
import com.sleekydz86.server.market.voucher.infrastructure.mapper.MemberCouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        memberCouponMapper.save(memberCoupon);
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
