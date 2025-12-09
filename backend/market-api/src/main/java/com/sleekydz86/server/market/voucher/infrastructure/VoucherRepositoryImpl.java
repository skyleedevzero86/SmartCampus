package com.sleekydz86.server.market.voucher.infrastructure;

import com.sleekydz86.server.market.voucher.domain.Voucher;
import com.sleekydz86.server.market.voucher.domain.VoucherRepository;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSimpleResponse;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSpecificResponse;
import com.sleekydz86.server.market.voucher.infrastructure.mapper.VoucherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VoucherRepositoryImpl implements VoucherRepository {

    private final VoucherMapper voucherMapper;
    private final VoucherQueryRepository voucherQueryRepository;

    @Override
    public Voucher save(final Voucher voucher) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("couponId", voucher.getCouponId());
        params.put("description", voucher.getDescription());
        params.put("voucherNumber", voucher.getVoucherNumber());
        params.put("isPublic", voucher.getIsPublic());
        params.put("isUsed", voucher.getIsUsed());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        voucherMapper.executeVoucherCUD(params);
        return voucher;
    }

    @Override
    public Optional<Voucher> findById(final Long voucherId) {
        return voucherMapper.findById(voucherId);
    }

    @Override
    public Page<VoucherSimpleResponse> findAllWithPaging(final Pageable pageable) {
        return voucherQueryRepository.findAllVouchers(pageable);
    }

    @Override
    public Optional<VoucherSpecificResponse> findSpecificVoucherById(final Long voucherId) {
        return voucherQueryRepository.findById(voucherId);
    }
}
