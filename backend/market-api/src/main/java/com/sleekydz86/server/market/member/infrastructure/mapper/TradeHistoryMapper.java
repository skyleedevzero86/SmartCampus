package com.sleekydz86.server.market.member.infrastructure.mapper;

import com.sleekydz86.server.market.member.domain.member.TradeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeHistoryMapper {

    void save(TradeHistory tradeHistory);

    List<TradeHistory> findAllByBuyerId(@Param("buyerId") Long buyerId);

    List<TradeHistory> findAllBySellerId(@Param("sellerId") Long sellerId);
}
