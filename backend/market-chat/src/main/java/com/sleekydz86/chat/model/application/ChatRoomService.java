package com.sleekydz86.chat.model.application;

import com.sleekydz86.chat.model.domain.Chat;
import com.sleekydz86.chat.model.domain.ChattingRoom;
import com.sleekydz86.chat.model.domain.ChattingRooms;
import com.sleekydz86.chat.model.domain.port.out.ChatPersistencePort;
import com.sleekydz86.chat.model.domain.port.out.ChattingRoomPersistencePort;
import com.sleekydz86.chat.model.domain.port.out.MessagePublishPort;
import com.sleekydz86.chat.model.domain.service.ChattingRoomDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChattingRoomDomainService chattingRoomDomainService;
    private final ChattingRoomPersistencePort chattingRoomPersistencePort;
    private final ChatPersistencePort chatPersistencePort;
    private final MessagePublishPort messagePublishPort;

    public ChattingRoom createChattingRoomByBuyer(
            final Long buyerId,
            final Long productId,
            final Long sellerId
    ) {
        return chattingRoomDomainService.createOrGetExistingChattingRoom(productId, buyerId, sellerId);
    }

    public Chat sendMessage(final Long chatRoomId, final ChatMessageRequest messageRequest) {
        Chat chat = Chat.create(chatRoomId, messageRequest.senderId(), messageRequest.message());
        Chat savedChat = chatPersistencePort.save(chat);

        messagePublishPort.publishMessage(chatRoomId, messageRequest);

        return savedChat;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ProductSoldEvent.class)
    public void handleProductSoldEvent(final ProductSoldEvent productSoldEvent) {
        List<ChattingRoom> foundChattingRooms = chattingRoomPersistencePort
                .findAllByProductId(productSoldEvent.getProductId());
        ChattingRooms chattingRooms = new ChattingRooms(foundChattingRooms);
        chattingRooms.completeAll();
    }
}

