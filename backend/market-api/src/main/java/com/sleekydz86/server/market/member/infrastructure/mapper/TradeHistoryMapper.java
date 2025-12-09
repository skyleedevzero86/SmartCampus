package com.sleekydz86.server.market.member.infrastructure.mapper;

import com.sleekydz86.server.market.member.domain.member.TradeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeHistoryMapper {

    List<TradeHistory> findAllByBuyerId(@Param("buyerId") Long buyerId);

    List<TradeHistory> findAllBySellerId(@Param("sellerId") Long sellerId);

    void executeTradeHistoryCUD(Map<String, Object> params);
}
