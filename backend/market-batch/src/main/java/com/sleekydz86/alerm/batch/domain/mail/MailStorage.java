package com.sleekydz86.alerm.batch.domain.mail;

import com.sleekydz86.alerm.batch.domain.mail.vo.MailStatus;
import com.sleekydz86.alerm.batch.domain.mail.vo.Receiver;
import com.sleekydz86.alerm.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailStorage extends BaseEntity {

    private Long id;

    private Receiver receiver;

    private MailStatus mailStatus;

    public static MailStorage createDefault(final Long memberId, final String email, final String nickname) {
        return getMailStorage(memberId, email, nickname, MailStatus.WAIT);
    }

    public static MailStorage createByStatus(final Long memberId, final String email, final String nickname, final MailStatus mailStatus) {
        return getMailStorage(memberId, email, nickname, mailStatus);
    }

    private static MailStorage getMailStorage(final Long memberId, final String email, final String nickname, final MailStatus mailStatus) {
        return MailStorage.builder()
                .receiver(Receiver.createDefault(memberId, email, nickname))
                .mailStatus(mailStatus)
                .build();
    }

    public void updateStatusFail() {
        this.mailStatus = MailStatus.FAIL;
    }

    public void updateStatusDone() {
        this.mailStatus = MailStatus.DONE;
    }

    public Long getReceiverId() {
        return receiver.getMemberId();
    }

    public String getReceiverEmail() {
        return receiver.getEmail();
    }

    public String getReceiverNickname() {
        return receiver.getNickname();
    }
}
