package com.sleekydz86.server.market.purchase.application;

import com.sleekydz86.server.market.coupon.domain.event.CouponPurchasedEvent;
import com.sleekydz86.server.market.purchase.domain.PurchaseHistory;
import com.sleekydz86.server.market.purchase.domain.PurchaseHistoryRepository;
import com.sleekydz86.server.market.voucher.domain.event.VoucherPurchasedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PurchaseEventHandler {

    private final PurchaseHistoryRepository purchaseHistoryRepository;

    @EventListener
    @Transactional
    public void handleCouponPurchased(final CouponPurchasedEvent event) {
        PurchaseHistory purchaseHistory = PurchaseHistory.createCouponPurchase(
                event.memberId(),
                event.couponId(),
                event.price()
        );
        purchaseHistoryRepository.save(purchaseHistory);
    }

    @EventListener
    @Transactional
    public void handleVoucherPurchased(final VoucherPurchasedEvent event) {
        PurchaseHistory purchaseHistory = PurchaseHistory.createVoucherPurchase(
                event.memberId(),
                event.voucherId(),
                event.price()
        );
        purchaseHistoryRepository.save(purchaseHistory);
    }
}
