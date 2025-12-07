package com.sleekydz86.server.market.coupon.application;

import com.sleekydz86.server.market.coupon.application.dto.MemberCouponCreateRequest;
import com.sleekydz86.server.market.product.domain.event.CouponExistValidatedEvent;
import com.sleekydz86.server.market.product.domain.event.UsedCouponDeletedEvent;
import com.sleekydz86.server.market.voucher.domain.event.UsedVoucherEvent;
import com.sleekydz86.server.market.voucher.domain.event.ValidatedExistedCouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CouponEventHandler {

    private final CouponService couponService;

    @EventListener
    public void validateMemberCouponExisted(final CouponExistValidatedEvent event) {
        couponService.validateMemberCouponsExisted(event.getMemberId(), event.getUsingCouponIds());
    }

    @EventListener
    public void deleteUsedMemberCoupons(final UsedCouponDeletedEvent event) {
        couponService.deleteUsedMemberCoupons(event.getBuyerId(), event.getUsedCoupons());
    }

    @EventListener
    public void validateCouponExisted(final ValidatedExistedCouponEvent event) {
        couponService.validateCouponExisted(event.couponId());
    }

    @EventListener
    public void addMemberCoupon(final UsedVoucherEvent event) {
        couponService.saveMemberCoupons(event.memberId(), new MemberCouponCreateRequest(List.of(event.couponId())));
    }
}
