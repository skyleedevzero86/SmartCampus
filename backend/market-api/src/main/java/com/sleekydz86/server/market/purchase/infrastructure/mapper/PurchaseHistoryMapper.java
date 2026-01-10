package com.sleekydz86.server.market.purchase.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PurchaseHistoryMapper {
    void executePurchaseHistoryCUD(Map<String, Object> params);
}
