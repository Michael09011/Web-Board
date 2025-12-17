package net.datasa.web5.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.CommentEntity;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.security.CustomUserDetails;
import net.datasa.web5.service.BoardService;
import net.datasa.web5.service.CommentService;
import net.datasa.web5.service.MemberService;

@Slf4j
@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    /**
     * 로그인 폼으로 이동
     */
    @GetMapping("login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "message", required = false) String message,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", message);
        }
        return "login";
    }

    /**
     * 회원가입 폼으로 이동
     */
    @GetMapping("join")
    public String join() {
        return "join";
    }

    /**
     * 회원가입 처리
     * @param member 사용자가 가입폼에 입력한 정보
     */
    @PostMapping("join")
    public String join(@ModelAttribute MemberEntity member) {
        memberService.join(member);
        return "redirect:/";
    }

    /**
     * 아이디 중복 확인
     * @param memberId 사용자가 입력한 아이디
     * @return 사용 가능하면 true, 아니면 false
     */
    @PostMapping("checkId")
    @ResponseBody
    public boolean checkId(@RequestParam("memberId") String memberId) {
        boolean isAvailable = memberService.isIdAvailable(memberId);
        return isAvailable;
    }

    /**
     * 로그아웃 처리
     */
//     @PostMapping("logout")
//     public String logout(HttpSession session) {
//         session.invalidate();
//         return "redirect:/";
//     }

    /**
     * 회원정보 수정 폼 이동
     */
    @GetMapping("update")
    public String updateForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("member", userDetails.getMember());
        return "member/update";
    }

    /**
     * 회원정보 수정 처리
     */
    @PostMapping("update")
    public String update(@ModelAttribute MemberEntity member, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 본인 확인 및 수정 로직 (비밀번호 변경 등)
        memberService.updateMember(member, userDetails.getUsername());
        return "redirect:/";
    }

    @GetMapping("/checkIdPopup")
    public String checkIdPopup() {
        return "member/checkIdPopup";
    }

    /**
     * 회원정보 페이지로 이동
     */
    @GetMapping("info")
    public String info(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("member", userDetails.getMember());
        return "member/info";
    }

    /**
     * 회원 탈퇴 처리
     * @param userDetails
     * @return
     */
    @PostMapping("delete")
    public String delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getUsername());
        return "redirect:/member/logout";
    }

    /**
     * 비밀번호 재설정 폼으로 이동
     */
    @GetMapping("findPassword")
    public String findPassword() {
        return "member/findPassword";
    }

    /**
     * 비밀번호 재설정 처리
     * @param memberId 아이디
     * @param email 이메일
     */
    @PostMapping("findPassword")
    public String findPassword(@RequestParam("memberId") String memberId, @RequestParam("email") String email, RedirectAttributes rttr) {
        String tempPassword = memberService.findPassword(memberId, email);
        rttr.addFlashAttribute("tempPassword", tempPassword);
        return "redirect:/member/login";
    }

    /**
     * 사용자 프로필 페이지로 이동
     * @param memberId 조회할 사용자의 아이디
     * @param model
     * @return
     */
    @GetMapping("profile/{memberId}")
    public String profile(@PathVariable("memberId") String memberId, Model model) {
        MemberEntity member = memberService.findMember(memberId);
        if (member == null) {
            // 사용자가 존재하지 않을 경우 예외 처리 또는 리다이렉트
            return "redirect:/";
        }
        java.util.List<BoardEntity> boards = boardService.findAllByMemberId(memberId);
        java.util.List<CommentEntity> comments = commentService.findAllByMemberId(memberId);

        model.addAttribute("member", member);
        model.addAttribute("boards", boards);
        model.addAttribute("comments", comments);

        return "member/profile";
    }
}
