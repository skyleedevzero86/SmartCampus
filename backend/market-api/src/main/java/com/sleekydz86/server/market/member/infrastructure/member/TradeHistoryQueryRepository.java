package com.sleekydz86.server.market.member.infrastructure.member;

import com.sleekydz86.server.market.member.domain.member.dto.TradeHistoryResponse;
import com.sleekydz86.server.market.member.infrastructure.mapper.TradeHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TradeHistoryQueryRepository {

    private final TradeHistoryMapper tradeHistoryMapper;

    public List<TradeHistoryResponse> findTradeHistories(final Long memberId, final boolean isSeller) {
        if (isSeller) {
            return tradeHistoryMapper.findTradeHistoriesBySellerId(memberId);
        }
        return tradeHistoryMapper.findTradeHistoriesByBuyerId(memberId);
    }
}
