package com.sleekydz86.server.market.member.infrastructure.member;

import com.sleekydz86.server.market.member.domain.member.Member;
import com.sleekydz86.server.market.member.domain.member.MemberRepository;
import com.sleekydz86.server.market.member.domain.member.dto.ProductByMemberResponse;
import com.sleekydz86.server.market.member.infrastructure.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        memberMapper.save(member);
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
