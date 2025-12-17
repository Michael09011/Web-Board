package net.datasa.web5.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.datasa.web5.domain.entity.NoticeCommentEntity;

public interface NoticeCommentRepository extends JpaRepository<NoticeCommentEntity, Integer> {
    List<NoticeCommentEntity> findAllByMemberMemberId(String memberId);
    List<NoticeCommentEntity> findByNoticeNoticeNumOrderByCommentId(Integer noticeNum);
    Integer countByNoticeNoticeNum(Integer noticeNum);
}
