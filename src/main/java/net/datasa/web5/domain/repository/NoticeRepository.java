package net.datasa.web5.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.datasa.web5.domain.entity.NoticeEntity;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {
    Page<NoticeEntity> findByNoticeTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<NoticeEntity> findByNoticeContentContainingIgnoreCase(String keyword, Pageable pageable);
    Page<NoticeEntity> findByMemberMemberNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<NoticeEntity> findByNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase(String title, String content, Pageable pageable);
    java.util.List<NoticeEntity> findTop5ByOrderByCreatedDateDesc();
}
