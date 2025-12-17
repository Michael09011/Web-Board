package net.datasa.web5.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.NoticeCommentDTO;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.entity.NoticeCommentEntity;
import net.datasa.web5.domain.entity.NoticeEntity;
import net.datasa.web5.domain.repository.MemberRepository;
import net.datasa.web5.domain.repository.NoticeCommentRepository;
import net.datasa.web5.domain.repository.NoticeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeCommentService {

    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(Integer noticeNum, String content) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        NoticeEntity notice = noticeRepository.findById(noticeNum)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice number: " + noticeNum));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        NoticeCommentEntity comment = NoticeCommentEntity.builder()
                .notice(notice)
                .member(member)
                .content(content)
                .build();
        noticeCommentRepository.save(comment);
    }

    public List<NoticeCommentDTO> getComments(Integer noticeNum) {
        return noticeCommentRepository.findByNoticeNoticeNumOrderByCommentId(noticeNum)
                .stream()
                .map(NoticeCommentDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer commentId) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        NoticeCommentEntity comment = noticeCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));

        if (!comment.getMember().getMemberId().equals(memberId) && !isAdmin) {
            throw new SecurityException("You do not have permission to delete this comment.");
        }

        noticeCommentRepository.delete(comment);
    }
}
