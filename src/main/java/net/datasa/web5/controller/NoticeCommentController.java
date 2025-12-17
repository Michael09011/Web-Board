package net.datasa.web5.controller;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.NoticeCommentDTO;
import net.datasa.web5.security.CustomUserDetails;
import net.datasa.web5.service.NoticeCommentService;

@RestController
@RequestMapping("notice-comment")
@RequiredArgsConstructor
public class NoticeCommentController {

    private final NoticeCommentService noticeCommentService;

    @PostMapping("{noticeNum}")
    public void createComment(@PathVariable("noticeNum") Integer noticeNum, 
                              @RequestBody NoticeCommentDTO dto,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            throw new AccessDeniedException("계정이 비활성화되어 댓글을 작성할 수 없습니다.");
        }
        noticeCommentService.create(noticeNum, dto.getContent());
    }

    @GetMapping("{noticeNum}")
    public List<NoticeCommentDTO> getComments(@PathVariable("noticeNum") Integer noticeNum) {
        return noticeCommentService.getComments(noticeNum);
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable("commentId") Integer commentId) {
        noticeCommentService.delete(commentId);
    }
}
