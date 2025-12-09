package com.sleekydz86.server.market.voucher.application;

import com.sleekydz86.server.global.exception.exceptions.coupon.VoucherNotFoundException;
import com.sleekydz86.server.market.voucher.domain.VoucherRepository;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSimpleResponse;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSpecificResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoucherQueryService {

    private final VoucherRepository voucherRepository;

    public Page<VoucherSimpleResponse> findAllWithPaging(final Pageable pageable) {
        return voucherRepository.findAllWithPaging(pageable);
    }

    public VoucherSpecificResponse findSpecificVoucher(final Long voucherId) {
        return voucherRepository.findSpecificVoucherById(voucherId)
                .orElseThrow(VoucherNotFoundException::new);
    }
}
