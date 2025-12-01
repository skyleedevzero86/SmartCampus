package com.sleekydz86.server.member.ui.member;

import com.sleekydz86.server.member.application.member.MemberService;
import com.sleekydz86.server.member.domain.member.dto.ProductByMemberResponse;
import com.sleekydz86.server.member.domain.member.dto.TradeHistoryResponse;
import com.sleekydz86.server.member.ui.auth.support.AuthMember;
import com.sleekydz86.server.member.ui.member.dto.MemberIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}/histories")
    public ResponseEntity<List<TradeHistoryResponse>> findTradeHistories(
            @PathVariable("memberId") final Long memberId,
            @AuthMember final Long authId,
            @RequestParam(value = "isSeller") final boolean isSeller
    ) {
        List<TradeHistoryResponse> response = memberService.findTradeHistories(memberId, authId, isSeller);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/products")
    public ResponseEntity<List<ProductByMemberResponse>> findProductHistories(
            @PathVariable("memberId") final Long memberId,
            @AuthMember final Long authId
    ) {
        return ResponseEntity.ok(memberService.findProductHistories(memberId, authId));
    }

    @GetMapping
    public ResponseEntity<MemberIdResponse> getMyId(@AuthMember final Long authId) {
        return ResponseEntity.ok(new MemberIdResponse(authId));
    }
}
