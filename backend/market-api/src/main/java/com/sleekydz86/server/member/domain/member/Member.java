package com.sleekydz86.server.member.domain.member;

import com.sleekydz86.server.global.domain.BaseEntity;
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
public class Member extends BaseEntity {

    private Long id;

    private String email;

    private String password;

    private String nickname;

    private MemberRole memberRole;

    public boolean isAdmin() {
        return this.memberRole.isAdministrator();
    }

    public static Member createDefaultRole(
            final String email,
            final String password,
            final NicknameGenerator nicknameGenerator
    ) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nicknameGenerator.createRandomNickname())
                .memberRole(MemberRole.MEMBER)
                .build();
    }

    public void validatePassword(final String password) {
        if (!this.password.equals(password)) {
            throw new PasswordNotMatchedException();
        }
    }

    public void validateAuth(final Long memberId) {
        if (!this.id.equals(memberId)) {
            throw new MemberAuthInvalidException();
        }
    }
}
