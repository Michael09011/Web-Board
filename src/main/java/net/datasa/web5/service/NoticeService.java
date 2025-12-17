package net.datasa.web5.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.NoticeDTO;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.entity.NoticeEntity;
import net.datasa.web5.domain.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<NoticeDTO> findAll(Pageable pageable) {
        Page<NoticeEntity> noticeEntities = noticeRepository.findAll(pageable);
        return noticeEntities.map(this::convertToNoticeDTO);
    }

    public NoticeEntity findById(Integer noticeNum) {
        return noticeRepository.findById(noticeNum).orElseThrow();
    }

    public void delete(Integer noticeNum) {
        noticeRepository.deleteById(noticeNum);
    }

    public void update(Integer noticeNum, String noticeTitle, String noticeContent) {
        NoticeEntity notice = noticeRepository.findById(noticeNum).orElseThrow();
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);
        noticeRepository.save(notice);
    }

    public Page<NoticeDTO> search(String type, String keyword, Pageable pageable) {
        Page<NoticeEntity> noticeEntities;
        switch (type) {
            case "title":
                noticeEntities = noticeRepository.findByNoticeTitleContainingIgnoreCase(keyword, pageable);
                break;
            case "content":
                noticeEntities = noticeRepository.findByNoticeContentContainingIgnoreCase(keyword, pageable);
                break;
            case "member":
                noticeEntities = noticeRepository.findByMemberMemberNameContainingIgnoreCase(keyword, pageable);
                break;
            case "title_content":
                noticeEntities = noticeRepository.findByNoticeTitleContainingIgnoreCaseOrNoticeContentContainingIgnoreCase(keyword, keyword, pageable);
                break;
            default:
                noticeEntities = noticeRepository.findAll(pageable);
        }
        return noticeEntities.map(this::convertToNoticeDTO);
    }

    private NoticeDTO convertToNoticeDTO(NoticeEntity noticeEntity) {
        return NoticeDTO.toDTO(noticeEntity);
    }

    public void save(NoticeEntity notice, MemberEntity member) {
        notice.setMember(member);
        noticeRepository.save(notice);
    }

    public java.util.List<NoticeDTO> findTop5() {
        return noticeRepository.findTop5ByOrderByCreatedDateDesc()
                .stream()
                .map(this::convertToNoticeDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
