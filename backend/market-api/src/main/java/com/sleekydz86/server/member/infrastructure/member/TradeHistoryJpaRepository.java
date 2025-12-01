package com.sleekydz86.server.member.infrastructure.member;

import com.sleekydz86.server.member.domain.member.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryJpaRepository extends JpaRepository<TradeHistory, Long> {

    TradeHistory save(TradeHistory tradeHistory);

    List<TradeHistory> findAllByBuyerId(Long buyerId);

    List<TradeHistory> findAllBySellerId(Long sellerId);
}
