package com.sleekydz86.server.market.voucher.infrastructure;

import com.sleekydz86.server.market.voucher.domain.dto.VoucherSimpleResponse;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSpecificResponse;
import com.sleekydz86.server.market.voucher.infrastructure.mapper.VoucherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VoucherQueryRepository {

    private final VoucherMapper voucherMapper;

    public Page<VoucherSimpleResponse> findAllVouchers(final Pageable pageable) {
        List<VoucherSimpleResponse> results = voucherMapper.findAllVouchers(
                pageable.getOffset(),
                pageable.getPageSize()
        );
        long total = voucherMapper.countAllVouchers();
        return new PageImpl<>(results, pageable, total);
    }

    public Optional<VoucherSpecificResponse> findById(final Long voucherId) {
        return voucherMapper.findVoucherById(voucherId);
    }
}
