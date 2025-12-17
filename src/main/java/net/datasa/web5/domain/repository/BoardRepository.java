package net.datasa.web5.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.datasa.web5.domain.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findAllByMemberMemberId(String memberId);
    Page<BoardEntity> findByBoardTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<BoardEntity> findByBoardContentContainingIgnoreCase(String keyword, Pageable pageable);
    Page<BoardEntity> findByMemberMemberNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<BoardEntity> findByBoardTitleContainingIgnoreCaseOrBoardContentContainingIgnoreCase(String title, String content, Pageable pageable);

    @Query("SELECT b FROM BoardEntity b LEFT JOIN FETCH b.member WHERE b.boardNum = :boardNum")
    Optional<BoardEntity> findByIdWithMember(@Param("boardNum") Integer boardNum);

    @Query("SELECT b FROM BoardEntity b LEFT JOIN FETCH b.member LEFT JOIN FETCH b.files WHERE b.boardNum = :boardNum")
    Optional<BoardEntity> findByIdWithMemberAndFiles(@Param("boardNum") Integer boardNum);

    @Query(value = "SELECT b FROM BoardEntity b LEFT JOIN FETCH b.files",
           countQuery = "SELECT count(b) FROM BoardEntity b")
    Page<BoardEntity> findAllWithFiles(Pageable pageable);

    @Modifying
    @Query("update BoardEntity b set b.hits = b.hits + 1 where b.boardNum = :boardNum")
    void updateHits(@Param("boardNum") Integer boardNum);
}
