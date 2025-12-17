package net.datasa.web5.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.datasa.web5.domain.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByMemberMemberId(String memberId);
    List<CommentEntity> findByBoardBoardNumOrderByCommentId(Integer boardNum);
    Integer countByBoardBoardNum(Integer boardNum);
}
