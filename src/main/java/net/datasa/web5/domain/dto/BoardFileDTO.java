package net.datasa.web5.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.web5.domain.entity.BoardFileEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardFileDTO {
    private Integer fileId;
    private String originalName;
    private String savedName;
    private long fileSize;
    private String contentType;

    public static BoardFileDTO toDTO(BoardFileEntity entity) {
        return BoardFileDTO.builder()
                .fileId(entity.getFileId())
                .originalName(entity.getOriginalName())
                .savedName(entity.getSavedName())
                .fileSize(entity.getFileSize())
                .contentType(entity.getContentType())
                .build();
    }
}
