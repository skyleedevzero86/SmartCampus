package com.sleekydz86.server.market.purchase.infrastructure;

import com.sleekydz86.server.market.purchase.domain.PurchaseHistory;
import com.sleekydz86.server.market.purchase.domain.PurchaseHistoryRepository;
import com.sleekydz86.server.market.purchase.infrastructure.mapper.PurchaseHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class PurchaseHistoryRepositoryImpl implements PurchaseHistoryRepository {

    private final PurchaseHistoryMapper purchaseHistoryMapper;

    @Override
    public PurchaseHistory save(final PurchaseHistory purchaseHistory) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("memberId", purchaseHistory.getMemberId());
        params.put("purchaseType", purchaseHistory.getPurchaseType().name());
        params.put("couponId", purchaseHistory.getCouponId());
        params.put("voucherId", purchaseHistory.getVoucherId());
        params.put("price", purchaseHistory.getPrice());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        purchaseHistoryMapper.executePurchaseHistoryCUD(params);
        return purchaseHistory;
    }
}
