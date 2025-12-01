package com.sleekydz86.server.member.infrastructure.member;

import com.sleekydz86.server.member.domain.member.TradeHistory;
import com.sleekydz86.server.member.domain.member.TradeHistoryRepository;
import com.sleekydz86.server.member.domain.member.dto.TradeHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TradeHistoryRepositoryImpl implements TradeHistoryRepository {

    private final TradeHistoryMapper tradeHistoryMapper;
    private final TradeHistoryQueryRepository tradeHistoryQueryRepository;

    @Override
    public TradeHistory save(final TradeHistory tradeHistory) {
        tradeHistoryMapper.save(tradeHistory);
        return tradeHistory;
    }

    @Override
    public List<TradeHistoryResponse> findHistories(final Long memberId, final boolean isSeller) {
        return tradeHistoryQueryRepository.findTradeHistories(memberId, isSeller);
    }
}
