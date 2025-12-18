package net.datasa.web5.service;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.entity.ReportEntity;
import net.datasa.web5.domain.entity.ReportStatus;
import net.datasa.web5.domain.repository.BoardRepository;
import net.datasa.web5.domain.repository.MemberRepository;
import net.datasa.web5.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public boolean createReport(Integer boardNum, String memberId) {
        BoardEntity board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        if (reportRepository.existsByBoardAndMember(board, member)) {
            return false; // 이미 신고한 경우
        }

        ReportEntity report = ReportEntity.builder()
                .board(board)
                .member(member)
                .status(ReportStatus.PENDING)
                .build();
        reportRepository.save(report);
        return true;
    }

    public List<ReportEntity> getAllReports() {
        return reportRepository.findAll();
    }

    @Transactional
    public void resolveReport(Integer reportId) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 없습니다."));
        report.setStatus(ReportStatus.RESOLVED);
    }

    @Transactional
    public void deleteReport(Integer reportId) {
        reportRepository.deleteById(reportId);
    }
}
