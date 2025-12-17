package net.datasa.web5.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.web5.domain.entity.NoticeCommentEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCommentDTO {
    private Integer commentId;
    private String memberId;
    private String content;
    private LocalDateTime createdDate;

    public static NoticeCommentDTO toDTO(NoticeCommentEntity entity) {
        return NoticeCommentDTO.builder()
                .commentId(entity.getCommentId())
                .memberId(entity.getMember().getMemberId())
                .content(entity.getContent())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
