package net.datasa.web5.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.web5.domain.entity.BoardEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Integer boardNum;
    private String memberId;
    private String memberName;
    private String boardTitle;
    private String boardContent;
    private Integer hits;
    private LocalDateTime createdDate;
    private Integer commentCount;
    private String thumbnail;
    private List<BoardFileDTO> files;

    public BoardEntity toEntity() {
        return BoardEntity.builder()
                .boardTitle(this.boardTitle)
                .boardContent(this.boardContent)
                .build();
    }

    public static BoardDTO toDTO(BoardEntity entity, Integer commentCount) {
        String thumbnail = null;
        if (entity.getFiles() != null && !entity.getFiles().isEmpty()) {
            // Find the first image file to use as a thumbnail
            for (int i = 0; i < entity.getFiles().size(); i++) {
                if (entity.getFiles().get(i).getContentType().startsWith("image")) {
                    thumbnail = entity.getFiles().get(i).getSavedName();
                    break;
                }
            }
        }

        List<BoardFileDTO> fileDTOs = entity.getFiles().stream()
                .map(BoardFileDTO::toDTO)
                .collect(Collectors.toList());

        return BoardDTO.builder()
                .boardNum(entity.getBoardNum())
                .memberId(entity.getMember().getMemberId())
                .memberName(entity.getMember().getMemberName())
                .boardTitle(entity.getBoardTitle())
                .boardContent(entity.getBoardContent())
                .hits(entity.getHits())
                .createdDate(entity.getCreatedDate())
                .commentCount(commentCount)
                .thumbnail(thumbnail)
                .files(fileDTOs)
                .build();
    }
}
