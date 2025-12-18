package net.datasa.web5.repository;

import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    boolean existsByBoardAndMember(BoardEntity board, MemberEntity member);
    void deleteByBoard(BoardEntity board);
}
