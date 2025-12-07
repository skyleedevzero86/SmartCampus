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

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VoucherRepositoryImpl implements VoucherRepository {

    private final VoucherMapper voucherMapper;
    private final VoucherQueryRepository voucherQueryRepository;

    @Override
    public Voucher save(final Voucher voucher) {
        voucherMapper.save(voucher);
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
