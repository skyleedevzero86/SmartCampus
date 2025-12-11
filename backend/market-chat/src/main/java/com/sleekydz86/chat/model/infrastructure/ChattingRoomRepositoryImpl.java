package com.sleekydz86.chat.model.infrastructure;

import com.sleekydz86.chat.model.domain.ChattingRoom;
import com.sleekydz86.chat.model.domain.dto.ChattingRoomSimpleResponse;
import com.sleekydz86.chat.model.domain.port.out.ChattingRoomPersistencePort;
import com.sleekydz86.chat.model.domain.port.out.ChattingRoomQueryPort;
import com.sleekydz86.chat.model.infrastructure.mapper.ChattingRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ChattingRoomRepositoryImpl implements ChattingRoomPersistencePort, ChattingRoomQueryPort {

    private final ChattingRoomMapper chattingRoomMapper;

    @Override
    public ChattingRoom save(final ChattingRoom chattingRoom) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("productId", chattingRoom.getProductId());
        params.put("buyerId", chattingRoom.getBuyerId());
        params.put("sellerId", chattingRoom.getSellerId());
        params.put("chattingStatus", chattingRoom.getChattingStatus() != null ? chattingRoom.getChattingStatus().name() : "PROCESS");
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        params.put("generatedId", null);
        
        chattingRoomMapper.executeChattingRoomCUD(params);
        
        Long generatedId = (Long) params.get("generatedId");
        if (generatedId != null) {
            return ChattingRoom.builder()
                    .id(generatedId)
                    .productId(chattingRoom.getProductId())
                    .buyerId(chattingRoom.getBuyerId())
                    .sellerId(chattingRoom.getSellerId())
                    .chattingStatus(chattingRoom.getChattingStatus())
                    .build();
        }
        
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

