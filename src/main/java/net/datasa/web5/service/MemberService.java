package net.datasa.web5.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.repository.MemberRepository;

@Slf4j

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     * @param member 사용자가 입력한 회원 정보
     */
    public void join(MemberEntity member) {
        String originalPassword = member.getMemberPassword();
        String encodedPassword = passwordEncoder.encode(originalPassword);

        MemberEntity newMember = MemberEntity.builder()
            .memberId(member.getMemberId())
            .memberPassword(encodedPassword)
            .memberName(member.getMemberName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .enabled(true)
                .rolename("ROLE_USER")
                .build();

        memberRepository.save(newMember);
    }

    /**
     * ID로 회원정보를 조회하여 가입 가능 여부 리턴
     * @param memberId 검색할 아이디
     * @return 가입 가능한 아이디면 true, 아니면 false
     */
    public boolean isIdAvailable(String memberId) {
        // ID로 회원정보를 조회해서 존재하면 false, 존재하지 않으면 true
        return !memberRepository.existsById(memberId);
    }

    /**
     * 모든 회원 목록을 페이징하여 반환합니다.
     * @param pageable 페이징 정보
     * @return 페이징된 회원 목록
     */
    public Page<MemberEntity> findAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * 검색어로 회원을 검색하여 페이징한 목록을 반환합니다.
     * @param keyword 검색어
     * @param pageable 페이징 정보
     * @return 검색된 페이징 회원 목록
     */
    public Page<MemberEntity> searchMembers(String keyword, Pageable pageable) {
        return memberRepository.findByMemberIdContainingOrMemberNameContaining(keyword, keyword, pageable);
    }

    /**
     * ID로 회원정보를 조회
     * @param memberId 검색할 아이디
     * @return 조회된 회원 정보
     */
    public MemberEntity findMember(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    /**
     * 특정 회원을 ID로 삭제합니다.
     * @param memberId 삭제할 회원의 ID
     */
    @Transactional
    public void deleteMember(String memberId) {
        // 관리자 계정("admin")은 삭제되지 않도록 보호합니다.
        if ("admin".equals(memberId)) {
            log.warn("관리자 계정은 삭제할 수 없습니다.");
            return;
        }
        memberRepository.deleteById(memberId);
    }

    /**
     * 회원 정보 수정
     * @param updated 수정할 정보가 담긴 MemberEntity
     * @param loginId 로그인한 회원의 ID
     */
    @Transactional
    public void updateMember(MemberEntity updated, String loginId) {
        MemberEntity member = memberRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + loginId));

        member.setMemberName(updated.getMemberName());
        member.setEmail(updated.getEmail());
        member.setPhone(updated.getPhone());
        member.setAddress(updated.getAddress());
        // 비밀번호 변경 시
        if (updated.getMemberPassword() != null && !updated.getMemberPassword().isBlank()) {
            String originalPassword = updated.getMemberPassword();
            member.setMemberPassword(passwordEncoder.encode(originalPassword));
        }
        memberRepository.save(member);
    }

    /**
     * 회원 계정 상태 변경 (활성/비활성)
     * @param memberId 대상 회원의 ID
     */
    @Transactional
    public void toggleMemberStatus(String memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + memberId));
        
        // 관리자 계정은 상태 변경 불가
        if ("admin".equals(memberId)) {
            log.warn("관리자 계정의 상태는 변경할 수 없습니다.");
            return;
        }

        member.setEnabled(!member.isEnabled());
        memberRepository.save(member);
    }

    /**
     * 회원 등급 변경 (관리자/일반회원)
     * @param memberId 대상 회원의 ID
     */
    @Transactional
    public void toggleMemberRole(String memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + memberId));

        // 관리자 계정은 등급 변경 불가
        if ("admin".equals(memberId)) {
            log.warn("관리자 계정의 등급은 변경할 수 없습니다.");
            return;
        }

        String currentRole = member.getRolename();
        String newRole = "ROLE_USER".equals(currentRole) ? "ROLE_ADMIN" : "ROLE_USER";
        member.setRolename(newRole);
        memberRepository.save(member);
    }

    /**
     * 비밀번호 재설정
     * @param memberId 아이디
     * @param email 이메일
     * @return 임시 비밀번호
     */
    @Transactional
    public String findPassword(String memberId, String email) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + memberId));

        if (!member.getEmail().equals(email)) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }

        // 임시 비밀번호 생성
        String tempPassword = getTempPassword();
        log.info("임시 비밀번호: {}", tempPassword);

        // 비밀번호 변경
        member.setMemberPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);

        // TODO: 이메일로 임시 비밀번호 발송

        return tempPassword;
    }

    /**
     * 임시 비밀번호 생성
     * @return 임시 비밀번호
     */
    private String getTempPassword() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}
