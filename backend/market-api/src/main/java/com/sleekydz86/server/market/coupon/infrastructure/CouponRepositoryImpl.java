package com.sleekydz86.server.market.coupon.infrastructure;

import com.sleekydz86.server.market.coupon.domain.Coupon;
import com.sleekydz86.server.market.coupon.domain.CouponRepository;
import com.sleekydz86.server.market.coupon.infrastructure.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponMapper couponMapper;

    @Override
    public Coupon save(final Coupon coupon) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("name", coupon.getDescription().getName());
        params.put("content", coupon.getDescription().getContent());
        params.put("canUseAlone", coupon.getPolicy().isCanUseAlone());
        params.put("isDiscountPercentage", coupon.getPolicy().isDiscountPercentage());
        params.put("amount", coupon.getPolicy().getAmount());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        couponMapper.executeCouponCUD(params);
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(final Long id) {
        return couponMapper.findById(id);
    }

    @Override
    public void deleteById(final Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "D");
        params.put("id", id);
        params.put("name", null);
        params.put("content", null);
        params.put("canUseAlone", null);
        params.put("isDiscountPercentage", null);
        params.put("amount", null);
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        couponMapper.executeCouponCUD(params);
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
