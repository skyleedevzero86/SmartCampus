package com.sleekydz86.chat.model.infrastructure.mapper;

import com.sleekydz86.chat.model.domain.ChattingRoom;
import com.sleekydz86.chat.model.domain.dto.ChattingRoomSimpleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ChattingRoomMapper {

    void save(ChattingRoom chattingRoom);

    Optional<ChattingRoom> findById(@Param("id") Long id);

    Optional<ChattingRoom> findBySellerIdAndBuyerIdAndProductId(
            @Param("sellerId") Long sellerId,
            @Param("buyerId") Long buyerId,
            @Param("productId") Long productId
    );

    List<ChattingRoom> findAllByProductId(@Param("productId") Long productId);

    List<ChattingRoomSimpleResponse> findMyChattingRooms(@Param("authId") Long authId);

    void executeChattingRoomCUD(Map<String, Object> params);
}
