package com.sleekydz86.server.member.member;


import com.sleekydz86.server.member.member.dto.TradeHistoryResponse;

import java.util.List;

public interface TradeHistoryRepository {

    TradeHistory save(TradeHistory tradeHistory);

    List<TradeHistoryResponse> findHistories(Long memberId, boolean isSeller);
}
