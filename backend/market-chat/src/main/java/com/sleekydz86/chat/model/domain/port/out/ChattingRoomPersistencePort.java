package com.sleekydz86.chat.model.domain.port.out;

import com.sleekydz86.chat.model.domain.ChattingRoom;

import java.util.List;
import java.util.Optional;

public interface ChattingRoomPersistencePort {

    ChattingRoom save(ChattingRoom chattingRoom);

    Optional<ChattingRoom> findBySellerIdAndBuyerIdAndProductId(Long sellerId, Long buyerId, Long productId);

    List<ChattingRoom> findAllByProductId(Long productId);
}

