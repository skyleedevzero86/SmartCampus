package com.sleekydz86.chat.model.ui;

import com.sleekydz86.chat.model.application.ChatRoomQueryService;
import com.sleekydz86.chat.model.application.ChatRoomService;
import com.sleekydz86.chat.model.application.dto.ChatMessageRequest;
import com.sleekydz86.chat.model.application.dto.ChattingRoomCreateRequest;
import com.sleekydz86.chat.model.domain.Chat;
import com.sleekydz86.chat.model.domain.ChattingRoom;
import com.sleekydz86.chat.model.domain.dto.ChatHistoryResponse;
import com.sleekydz86.chat.model.domain.dto.ChattingRoomSimpleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomQueryService chatRoomQueryService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/api/chats")
    public ResponseEntity<List<ChattingRoomSimpleResponse>> findAllMyChattingRooms(
            @RequestParam("authId") final Long authId
    ) {
        return ResponseEntity.ok(chatRoomQueryService.findAllMyChats(authId));
    }

    @PostMapping("/api/products/{productId}/chats")
    public ResponseEntity<ChattingRoomResponse> createChattingRoomByBuyer(
            @PathVariable final Long productId,
            @RequestParam("authMember") final Long authMember,
            @RequestBody @Valid final ChattingRoomCreateRequest request
    ) {
        ChattingRoom chattingRoom = chatRoomService.createChattingRoomByBuyer(
                authMember, productId, request.sellerId()
        );
        return ResponseEntity.created(
                URI.create("/api/products/" + productId + "/chats/" + chattingRoom.getId())
        ).body(ChattingRoomResponse.from(chattingRoom));
    }

    @GetMapping("/api/products/{productId}/chats/{chattingRoomId}")
    public ResponseEntity<List<ChatHistoryResponse>> findChatHistoryByChattingRoomId(
            @RequestParam("authId") final Long authId,
            @PathVariable final Long productId,
            @PathVariable final Long chattingRoomId,
            @RequestParam(name = "chatId", required = false) final Long chatId,
            @RequestParam(name = "pageSize") final Integer pageSize
    ) {
        return ResponseEntity.ok(
                chatRoomQueryService.findChattingHistoryByChatId(authId, chattingRoomId, chatId, pageSize)
        );
    }

    @MessageMapping("/chats/{chatRoomId}/messages")
    public ChatMessageResponse handleChatMessage(
            @DestinationVariable final Long chatRoomId,
            final ChatMessageRequest chattingRequest
    ) {
        Chat chat = chatRoomService.sendMessage(chatRoomId, chattingRequest);
        return ChatMessageResponse.from(chat);
    }
}

