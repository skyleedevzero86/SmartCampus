package com.sleekydz86.server.market.coupon.infrastructure.mapper;

import com.sleekydz86.server.market.coupon.domain.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface CouponMapper {

    Optional<Coupon> findById(@Param("id") Long id);

    List<Coupon> findAllByIdIn(@Param("ids") List<Long> ids);

    int countAllByIdIn(@Param("ids") List<Long> ids);

    void executeCouponCUD(Map<String, Object> params);
}


