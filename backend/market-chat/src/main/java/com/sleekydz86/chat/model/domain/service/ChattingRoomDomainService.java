package com.sleekydz86.chat.model.domain.service;

import com.sleekydz86.chat.model.domain.ChattingRoom;

import java.util.Optional;


public class ChattingRoomDomainService {

    private final ChattingRoomPersistencePort chattingRoomPersistencePort;

    public ChattingRoomDomainService(ChattingRoomPersistencePort chattingRoomPersistencePort) {
        this.chattingRoomPersistencePort = chattingRoomPersistencePort;
    }

    public ChattingRoom createOrGetExistingChattingRoom(
            Long productId,
            Long buyerId,
            Long sellerId
    ) {
        Optional<ChattingRoom> existingRoom = chattingRoomPersistencePort
                .findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);

        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        ChattingRoom newRoom = ChattingRoom.createNewChattingRoom(productId, buyerId, sellerId);
        return chattingRoomPersistencePort.save(newRoom);
    }
}





