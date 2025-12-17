package net.datasa.web5.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.BoardFileEntity;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.repository.BoardFileRepository;
import net.datasa.web5.domain.repository.BoardRepository;
import net.datasa.web5.domain.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Value("${file.upload.path}")
    private String uploadPath;

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final CommentRepository commentRepository;

    public Page<BoardDTO> findAll(Pageable pageable) {
        Page<BoardEntity> boardEntities = boardRepository.findAllWithFiles(pageable);
        return boardEntities.map(this::convertToBoardDTO);
    }

    public List<BoardEntity> findAllByMemberId(String memberId) {
        return boardRepository.findAllByMemberMemberId(memberId);
    }

    @Transactional
    public BoardEntity findById(Integer boardNum) {
        boardRepository.updateHits(boardNum);
        return boardRepository.findByIdWithMemberAndFiles(boardNum).orElseThrow();
    }


    public void delete(Integer boardNum) {
        BoardEntity board = boardRepository.findByIdWithMember(boardNum).orElseThrow();

        // 파일 시스템에서 파일 삭제
        for (BoardFileEntity file : board.getFiles()) {
            try {
                Path filePath = Paths.get(uploadPath, file.getSavedName());
                Files.deleteIfExists(filePath);
            } catch (Exception e) {
                // 예외 처리 (로그 등)
                e.printStackTrace();
            }
        }
        boardRepository.deleteById(boardNum);
    }

    @Transactional
    public void update(Integer boardNum, String boardTitle, String boardContent, List<Integer> deleteFileIds, MultipartFile[] files) {
        BoardEntity board = boardRepository.findByIdWithMemberAndFiles(boardNum).orElseThrow();
        board.setBoardTitle(boardTitle);
        board.setBoardContent(boardContent);

        // 파일 삭제
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            List<BoardFileEntity> filesToDelete = board.getFiles().stream()
                    .filter(f -> deleteFileIds.contains(f.getFileId()))
                    .toList();

            for (BoardFileEntity fileEntity : filesToDelete) {
                try {
                    Path filePath = Paths.get(uploadPath, fileEntity.getSavedName());
                    Files.deleteIfExists(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            board.getFiles().removeIf(file -> deleteFileIds.contains(file.getFileId()));
        }

        // 새 파일 추가
        saveFiles(board, files);
        
        boardRepository.save(board);
    }

    private void saveFiles(BoardEntity board, MultipartFile[] files) {
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    String originalName = file.getOriginalFilename();
                    String savedName = System.currentTimeMillis() + "_" + originalName;
                    Path uploadDir = Paths.get(uploadPath);
                    if (!Files.exists(uploadDir)) {
                        Files.createDirectories(uploadDir);
                    }
                    Path filePath = uploadDir.resolve(savedName);
                    file.transferTo(filePath.toFile());

                    BoardFileEntity boardFile = BoardFileEntity.builder()
                        .board(board)
                        .originalName(originalName)
                        .savedName(savedName)
                        .fileSize(file.getSize())
                        .contentType(file.getContentType())
                        .build();
                    board.getFiles().add(boardFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Page<BoardDTO> search(String type, String keyword, Pageable pageable) {
        Page<BoardEntity> boardEntities;
        switch (type) {
            case "title":
                boardEntities = boardRepository.findByBoardTitleContainingIgnoreCase(keyword, pageable);
                break;
            case "content":
                boardEntities = boardRepository.findByBoardContentContainingIgnoreCase(keyword, pageable);
                break;
            case "member":
                boardEntities = boardRepository.findByMemberMemberNameContainingIgnoreCase(keyword, pageable);
                break;
            case "title_content":
                boardEntities = boardRepository.findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(keyword, keyword, pageable);
                break;
            default:
                boardEntities = boardRepository.findAll(pageable);
        }
        return boardEntities.map(this::convertToBoardDTO);
    }

    private BoardDTO convertToBoardDTO(BoardEntity boardEntity) {
        return BoardDTO.toDTO(boardEntity, boardEntity.getComments().size());
    }

    public BoardFileEntity findFileByFileNum(Integer fileNum) {
        return boardFileRepository.findById(fileNum).orElse(null);
    }

    @Transactional
    public void save(BoardEntity board, MultipartFile[] files, MemberEntity member) {
        board.setMember(member);
        board.setHits(0); // 조회수 초기값 설정
        saveFiles(board, files);
        boardRepository.save(board);
    }

}
