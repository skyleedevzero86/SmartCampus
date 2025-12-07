package com.sleekydz86.server.market.voucher.infrastructure;

import com.sleekydz86.server.market.voucher.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherJpaRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findById(Long id);

    Voucher save(Voucher voucher);
}
