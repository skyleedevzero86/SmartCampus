package com.sleekydz86.chat.model.domain;

import com.sleekydz86.chat.model.domain.vo.ChattingStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChattingRoom extends BaseEntity {

    private Long id;

    private Long productId;

    private Long buyerId;

    private Long sellerId;

    private ChattingStatus chattingStatus;

    public static ChattingRoom createNewChattingRoom(
            final Long productId,
            final Long buyerId,
            final Long sellerId
    ) {
        validateParticipants(buyerId, sellerId);
        validateProduct(productId);
        return ChattingRoom.builder()
                .productId(productId)
                .buyerId(buyerId)
                .sellerId(sellerId)
                .chattingStatus(ChattingStatus.PROCESS)
                .build();
    }

    public void complete() {
        if (this.chattingStatus == ChattingStatus.DONE) {
            throw new IllegalStateException("이미 종료된 채팅방입니다.");
        }
        this.chattingStatus = ChattingStatus.DONE;
    }

    public boolean isInProgress() {
        return this.chattingStatus == ChattingStatus.PROCESS;
    }

    public boolean isCompleted() {
        return this.chattingStatus == ChattingStatus.DONE;
    }


    public boolean isParticipant(Long memberId) {
        return this.buyerId.equals(memberId) || this.sellerId.equals(memberId);
    }

    private static void validateParticipants(Long buyerId, Long sellerId) {
        if (buyerId == null || buyerId <= 0) {
            throw new IllegalArgumentException("구매자 ID는 양수여야 합니다.");
        }
        if (sellerId == null || sellerId <= 0) {
            throw new IllegalArgumentException("판매자 ID는 양수여야 합니다.");
        }
        if (buyerId.equals(sellerId)) {
            throw new IllegalArgumentException("구매자와 판매자는 동일할 수 없습니다.");
        }
    }

    private static void validateProduct(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("상품 ID는 양수여야 합니다.");
        }
    }
}

