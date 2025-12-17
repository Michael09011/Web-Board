package net.datasa.web5.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.web5.domain.entity.NoticeEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Integer noticeNum;
    private String memberName;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime createdDate;
    private Integer commentCount;

    public static NoticeDTO toDTO(NoticeEntity entity) {
        return NoticeDTO.builder()
                .noticeNum(entity.getNoticeNum())
                .memberName(entity.getMember().getMemberName())
                .noticeTitle(entity.getNoticeTitle())
                .noticeContent(entity.getNoticeContent())
                .createdDate(entity.getCreatedDate())
                .commentCount(0) // 댓글 기능 추가 시 수정 필요
                .build();
    }
}
