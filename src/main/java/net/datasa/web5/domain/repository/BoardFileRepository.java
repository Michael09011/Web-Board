package net.datasa.web5.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.datasa.web5.domain.entity.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Integer> {
    // 필요시 커스텀 쿼리 추가
}
