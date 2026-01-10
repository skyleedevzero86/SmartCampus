package com.sleekydz86.server.market.voucher.application;

import com.sleekydz86.server.global.event.Events;
import com.sleekydz86.server.global.exception.exceptions.coupon.CouponNotFoundException;
import com.sleekydz86.server.global.exception.exceptions.coupon.VoucherNotFoundException;
import com.sleekydz86.server.market.coupon.domain.Coupon;
import com.sleekydz86.server.market.coupon.domain.CouponRepository;
import com.sleekydz86.server.market.coupon.application.CouponService;
import com.sleekydz86.server.market.coupon.application.dto.MemberCouponCreateRequest;
import com.sleekydz86.server.market.voucher.application.dto.VoucherCreateRequest;
import com.sleekydz86.server.market.voucher.application.dto.VoucherNumberRequest;
import com.sleekydz86.server.market.voucher.domain.Voucher;
import com.sleekydz86.server.market.voucher.domain.VoucherNumberGenerator;
import com.sleekydz86.server.market.voucher.domain.VoucherRepository;
import com.sleekydz86.server.market.voucher.domain.event.UsedVoucherEvent;
import com.sleekydz86.server.market.voucher.domain.event.ValidatedExistedCouponEvent;
import com.sleekydz86.server.market.voucher.domain.event.VoucherPurchasedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherNumberGenerator voucherNumberGenerator;
    private final CouponRepository couponRepository;
    private final CouponService couponService;

    public Voucher savePrivateVoucher(final VoucherCreateRequest request) {
        Events.raise(new ValidatedExistedCouponEvent(request.couponId()));
        Voucher voucher = Voucher.createPrivate(request.couponId(), request.description(), voucherNumberGenerator);
        return voucherRepository.save(voucher);
    }

    public void useVoucher(final Long voucherId, final VoucherNumberRequest request, final Long memberId) {
        Voucher voucher = findVoucher(voucherId);
        voucher.use(request.voucherNumber());
        Events.raise(new UsedVoucherEvent(voucher.getCouponId(), memberId));
    }

    public Voucher purchaseVoucher(final Long memberId, final Long couponId, final String description) {
        Events.raise(new ValidatedExistedCouponEvent(couponId));
        
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(CouponNotFoundException::new);
        
        Voucher voucher = Voucher.createPrivate(couponId, description, voucherNumberGenerator);
        Voucher savedVoucher = voucherRepository.save(voucher);
        
        couponService.saveMemberCoupons(memberId, new MemberCouponCreateRequest(List.of(couponId)));
        
        Events.raise(new VoucherPurchasedEvent(memberId, savedVoucher.getId(), coupon.getPrice()));
        
        return savedVoucher;
    }

    private Voucher findVoucher(final Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(VoucherNotFoundException::new);
    }
}
