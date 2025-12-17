package net.datasa.web5.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.service.BoardService;
import net.datasa.web5.service.MemberService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 관리자 메인 페이지
    @GetMapping("")
    public String adminHome() {
        return "admin/index";
    }

    // 회원 목록 조회
    @GetMapping("/members")
    public String listMembers(
            @PageableDefault(page = 0, size = 10, sort = "rolename", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "keyword", defaultValue = "") String keyword, 
            Model model) {
        
        Page<MemberEntity> memberPage;
        if (keyword.isEmpty()) {
            memberPage = memberService.findAllMembers(pageable);
        } else {
            memberPage = memberService.searchMembers(keyword, pageable);
        }
        model.addAttribute("memberPage", memberPage);
        model.addAttribute("keyword", keyword);
        return "admin/memberList";
    }

    // 회원 삭제
    @PostMapping("/members/delete/{memberId}")
    public String deleteMember(@PathVariable("memberId") String memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/admin/members";
    }

    // 회원 수정 폼으로 이동
    @GetMapping("/members/update/{memberId}")
    public String updateMemberForm(@PathVariable("memberId") String memberId, Model model) {
        MemberEntity member = memberService.findMember(memberId);
        model.addAttribute("member", member);
        return "admin/updateMember";
    }

    // 회원 정보 수정 처리
    @PostMapping("/members/update")
    public String updateMember(MemberEntity member) {
        memberService.updateMember(member, member.getMemberId());
        return "redirect:/admin/members";
    }

    // 회원 계정 상태 변경
    @PostMapping("/members/toggle-status/{memberId}")
    public String toggleMemberStatus(@PathVariable("memberId") String memberId) {
        memberService.toggleMemberStatus(memberId);
        return "redirect:/admin/members";
    }

    // 회원 등급 변경
    @PostMapping("/members/toggle-role/{memberId}")
    public String toggleMemberRole(@PathVariable("memberId") String memberId) {
        memberService.toggleMemberRole(memberId);
        return "redirect:/admin/members";
    }

    // 게시글 목록 조회
    @GetMapping("/boards")
    public String listBoards(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "type", defaultValue = "") String type,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            Model model) {

        Page<BoardDTO> boardPage;
        if (type.isEmpty() || keyword.isEmpty()) {
            boardPage = boardService.findAll(pageable);
        } else {
            boardPage = boardService.search(type, keyword, pageable);
        }

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "admin/boardList";
    }
}
