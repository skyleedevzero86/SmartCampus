package com.sleekydz86.server.market.member.domain.member;

import com.sleekydz86.server.global.exception.exceptions.member.RoleNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MemberRole {

    MEMBER("member"),
    ADMIN("admin");

    private final String role;

    MemberRole(final String role) {
        this.role = role;
    }

    public static MemberRole from(final String role) {
        return Arrays.stream(values())
                .filter(value -> value.role.equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(RoleNotFoundException::new);
    }

    public boolean isAdministrator() {
        return this.equals(ADMIN);
    }
}
