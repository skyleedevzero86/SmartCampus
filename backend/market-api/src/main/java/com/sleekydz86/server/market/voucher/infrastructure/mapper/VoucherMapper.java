package com.sleekydz86.server.market.voucher.infrastructure.mapper;

import com.sleekydz86.server.market.voucher.domain.Voucher;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSimpleResponse;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSpecificResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface VoucherMapper {

    Optional<Voucher> findById(@Param("id") Long id);

    void executeVoucherCUD(Map<String, Object> params);

    java.util.List<VoucherSimpleResponse> findAllVouchers(@Param("offset") long offset, @Param("limit") int limit);

    long countAllVouchers();

    Optional<VoucherSpecificResponse> findVoucherById(@Param("voucherId") Long voucherId);
}


