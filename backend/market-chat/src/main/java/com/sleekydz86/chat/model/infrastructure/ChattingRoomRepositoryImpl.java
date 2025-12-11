package com.sleekydz86.chat.model.infrastructure;

import com.sleekydz86.chat.model.domain.ChattingRoom;
import com.sleekydz86.chat.model.domain.dto.ChattingRoomSimpleResponse;
import com.sleekydz86.chat.model.domain.port.out.ChattingRoomPersistencePort;
import com.sleekydz86.chat.model.domain.port.out.ChattingRoomQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ChattingRoomRepositoryAdapter implements ChattingRoomPersistencePort, ChattingRoomQueryPort {

    private final ChattingRoomMapper chattingRoomMapper;

    @Override
    public ChattingRoom save(final ChattingRoom chattingRoom) {
        chattingRoomMapper.save(chattingRoom);
        return chattingRoom;
    }

    @Override
    public List<ChattingRoomSimpleResponse> findMyChattingRooms(final Long authId) {
        return chattingRoomMapper.findMyChattingRooms(authId);
    }

    @Override
    public Optional<ChattingRoom> findBySellerIdAndBuyerIdAndProductId(
            final Long sellerId,
            final Long buyerId,
            final Long productId
    ) {
        return chattingRoomMapper.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);
    }

    @Override
    public List<ChattingRoom> findAllByProductId(final Long productId) {
        return chattingRoomMapper.findAllByProductId(productId);
    }
}

