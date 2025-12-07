package com.sleekydz86.server.market.voucher.domain;

import com.sleekydz86.server.market.voucher.domain.dto.VoucherSimpleResponse;
import com.sleekydz86.server.market.voucher.domain.dto.VoucherSpecificResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VoucherRepository {

    Voucher save(Voucher voucher);

    Optional<Voucher> findById(Long voucherId);

    Page<VoucherSimpleResponse> findAllWithPaging(Pageable pageable);

    Optional<VoucherSpecificResponse> findSpecificVoucherById(Long voucherId);
}
