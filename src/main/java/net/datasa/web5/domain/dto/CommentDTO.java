package net.datasa.web5.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.web5.domain.entity.CommentEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer commentId;
    private String memberId;
    private String content;
    private LocalDateTime createdDate;

    public static CommentDTO toDTO(CommentEntity entity) {
        return CommentDTO.builder()
                .commentId(entity.getCommentId())
                .memberId(entity.getMember().getMemberId())
                .content(entity.getContent())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
