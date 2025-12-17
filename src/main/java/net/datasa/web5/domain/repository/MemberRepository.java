package net.datasa.web5.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.datasa.web5.domain.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    /**
     * 아이디 또는 이름에 검색어가 포함된 회원을 조회
     * @param memberId 아이디 검색어
     * @param memberName 이름 검색어
     * @param pageable 페이지 정보
     * @return 페이징 처리된 회원 목록
     */
    Page<MemberEntity> findByMemberIdContainingOrMemberNameContaining(String memberId, String memberName, Pageable pageable);
}
