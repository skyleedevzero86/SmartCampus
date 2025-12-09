package com.sleekydz86.server.market.member.infrastructure.member;

import com.sleekydz86.server.market.member.domain.member.Member;
import com.sleekydz86.server.market.member.domain.member.MemberRepository;
import com.sleekydz86.server.market.member.domain.member.dto.ProductByMemberResponse;
import com.sleekydz86.server.market.member.infrastructure.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberMapper memberMapper;
    private final MemberQueryRepository memberQueryRepository;

    @Override
    public Optional<Member> findById(final Long id) {
        return memberMapper.findById(id);
    }

    @Override
    public Optional<Member> findByNickname(final String nickname) {
        return memberMapper.findByNickname(nickname);
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return memberMapper.findByEmail(email);
    }

    @Override
    public Member save(final Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", "C");
        params.put("id", null);
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        params.put("nickname", member.getNickname());
        params.put("memberRole", member.getMemberRole().name());
        params.put("resultMessage", null);
        params.put("affectedRows", null);
        memberMapper.executeMemberCUD(params);
        return member;
    }

    @Override
    public boolean existsByEmail(final String email) {
        return memberMapper.existsByEmail(email);
    }

    @Override
    public List<ProductByMemberResponse> findProductsByMemberId(final Long memberId) {
        return memberQueryRepository.findProductsByMemberId(memberId);
    }
}
