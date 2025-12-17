package net.datasa.web5.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.CommentDTO;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.CommentEntity;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.repository.BoardRepository;
import net.datasa.web5.domain.repository.CommentRepository;
import net.datasa.web5.domain.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(Integer boardNum, String content) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        BoardEntity board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board number: " + boardNum));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        CommentEntity comment = CommentEntity.builder()
                .board(board)
                .member(member)
                .content(content)
                .build();
        commentRepository.save(comment);
    }

    public List<CommentDTO> getComments(Integer boardNum) {
        return commentRepository.findByBoardBoardNumOrderByCommentId(boardNum)
                .stream()
                .map(CommentDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<CommentEntity> findAllByMemberId(String memberId) {
        return commentRepository.findAllByMemberMemberId(memberId);
    }

    @Transactional
    public void delete(Integer commentId) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));

        if (!comment.getMember().getMemberId().equals(memberId) && !isAdmin) {
            throw new SecurityException("You do not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
    }
}
