package com.sleekydz86.server.market.member.infrastructure.mapper;

import com.sleekydz86.server.market.member.domain.member.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    Optional<Member> findById(@Param("id") Long id);

    Optional<Member> findByEmail(@Param("email") String email);

    Optional<Member> findByNickname(@Param("nickname") String nickname);

    boolean existsByEmail(@Param("email") String email);

    void executeMemberCUD(Map<String, Object> params);
}

