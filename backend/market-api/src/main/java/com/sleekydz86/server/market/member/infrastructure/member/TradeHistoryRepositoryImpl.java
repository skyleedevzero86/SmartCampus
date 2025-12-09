package com.sleekydz86.server.market.member.infrastructure.member;

import com.sleekydz86.server.market.member.domain.member.TradeHistory;
import com.sleekydz86.server.market.member.domain.member.TradeHistoryRepository;
import com.sleekydz86.server.market.member.domain.member.dto.TradeHistoryResponse;
import com.sleekydz86.server.market.member.infrastructure.mapper.TradeHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class TradeHistoryRepositoryImpl implements TradeHistoryRepository {

    private final TradeHistoryMapper tradeHistoryMapper;
    private final TradeHistoryQueryRepository tradeHistoryQueryRepository;

    @Override
    public TradeHistory save(final TradeHistory tradeHistory) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("buyerId", tradeHistory.getBuyerId());
        params.put("sellerId", tradeHistory.getSellerId());
        params.put("productId", tradeHistory.getProductId());
        params.put("productOriginPrice", tradeHistory.getProductOriginPrice());
        params.put("productDiscountPrice", tradeHistory.getProductDiscountPrice());
        params.put("usingCouponIds", tradeHistory.getUsingCouponIds());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        tradeHistoryMapper.executeTradeHistoryCUD(params);
        return tradeHistory;
    }

    @Override
    public List<TradeHistoryResponse> findHistories(final Long memberId, final boolean isSeller) {
        return tradeHistoryQueryRepository.findTradeHistories(memberId, isSeller);
    }
}
