package com.sleekydz86.server.market.coupon.application;

import com.sleekydz86.server.market.coupon.application.dto.CouponCreateRequest;
import com.sleekydz86.server.market.coupon.application.dto.MemberCouponCreateRequest;
import com.sleekydz86.server.market.coupon.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final ApplyPolicy applyPolicy;

    @Transactional
    public Long saveNewCoupon(final CouponCreateRequest request) {
        Coupon coupon = Coupon.createCoupon(request.name(), request.content(), request.canUseAlone(), request.isDiscountPercentage(), request.amount());
        return couponRepository.save(coupon)
                .getId();
    }

    @Transactional
    public void saveMemberCoupons(final Long memberId, final MemberCouponCreateRequest request) {
        List<Long> couponIds = request.couponIds();
        validateExistedInCouponsIds(couponIds);

        MemberCoupons memberCoupons = MemberCoupons.of(memberId, request.couponIds());
        memberCouponRepository.insertBulk(memberCoupons.getMemberCoupons());
    }

    private void validateExistedInCouponsIds(final List<Long> couponIds) {
        if (couponRepository.countAllByIdIn(couponIds) != couponIds.size()) {
            throw new CouponNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public Coupons findAllMemberCoupons(final Long memberId, final Long authId) {
        validateAuthentication(memberId, authId);
        List<Coupon> coupons = findCouponsByMemberId(memberId);
        return new Coupons(coupons);
    }

    private List<Coupon> findCouponsByMemberId(final Long memberId) {
        List<Long> couponIds = memberCouponRepository.findAllByMemberId(memberId).stream()
                .map(MemberCoupon::getCouponId)
                .toList();

        return couponRepository.findAllByIdsIn(couponIds);
    }

    private void validateAuthentication(final Long memberId, final Long authId) {
        if (!memberId.equals(authId)) {
            throw new AuthenticationInvalidException();
        }
    }

    @Transactional(readOnly = true)
    public void validateMemberCouponsExisted(final Long memberId, final List<Long> usingCouponIds) {
        int count = memberCouponRepository.countMemberIdWithCouponIds(memberId, usingCouponIds);
        validateCouponSize(usingCouponIds, count);
    }

    private void validateCouponSize(final List<Long> usingCouponIds, final int count) {
        if (usingCouponIds.size() != count) {
            throw new MemberCouponSizeNotEqualsException();
        }
    }

    @Transactional
    public void deleteUsedMemberCoupons(final Long buyerId, final List<Long> usedCoupons) {
        memberCouponRepository.deleteByMemberIdAndCouponIdIn(buyerId, usedCoupons);
    }

    @Transactional(readOnly = true)
    public int applyCoupons(final Integer productPrice, final List<Long> couponIds) {
        Coupons coupons = new Coupons(couponRepository.findAllByIdsIn(couponIds));
        coupons.validateContainsNotExistedCoupon(couponIds);
        return coupons.applyCoupons(productPrice, applyPolicy);
    }

    @Transactional(readOnly = true)
    public void validateCouponExisted(final Long couponId) {
        if (!couponRepository.isExistedById(couponId)) {
            throw new CouponNotFoundException();
        }
    }
}
