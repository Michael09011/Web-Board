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
import net.datasa.web5.domain.dto.CommentDTO;
import net.datasa.web5.security.CustomUserDetails;
import net.datasa.web5.service.CommentService;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("{boardNum}")
    public void createComment(@PathVariable("boardNum") Integer boardNum, 
                              @RequestBody CommentDTO dto,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            throw new AccessDeniedException("계정이 비활성화되어 댓글을 작성할 수 없습니다.");
        }
        commentService.create(boardNum, dto.getContent());
    }

    @GetMapping("{boardNum}")
    public List<CommentDTO> getComments(@PathVariable("boardNum") Integer boardNum) {
        return commentService.getComments(boardNum);
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable("commentId") Integer commentId) {
        commentService.delete(commentId);
    }
}
