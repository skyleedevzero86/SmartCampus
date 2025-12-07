package com.sleekydz86.server.market.coupon.infrastructure;

import com.sleekydz86.server.market.coupon.domain.Coupon;
import com.sleekydz86.server.market.coupon.domain.CouponRepository;
import com.sleekydz86.server.market.coupon.infrastructure.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponMapper couponMapper;

    @Override
    public Coupon save(final Coupon coupon) {
        couponMapper.save(coupon);
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(final Long id) {
        return couponMapper.findById(id);
    }

    @Override
    public void deleteById(final Long id) {
        couponMapper.deleteById(id);
    }

    @Override
    public List<Coupon> findAllByIdsIn(final List<Long> couponIds) {
        return couponMapper.findAllByIdIn(couponIds);
    }

    @Override
    public int countAllByIdIn(final List<Long> couponIds) {
        return couponMapper.countAllByIdIn(couponIds);
    }

    @Override
    public boolean isExistedById(final Long couponId) {
        return couponMapper.findById(couponId).isPresent();
    }
}
