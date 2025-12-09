package com.sleekydz86.server.market.voucher.infrastructure.mapper;

import com.sleekydz86.server.market.voucher.domain.Voucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface VoucherMapper {

    Optional<Voucher> findById(@Param("id") Long id);

    void executeVoucherCUD(Map<String, Object> params);
}


