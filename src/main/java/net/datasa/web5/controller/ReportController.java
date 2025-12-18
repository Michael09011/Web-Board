package net.datasa.web5.controller;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.entity.ReportEntity;
import net.datasa.web5.security.CustomUserDetails;
import net.datasa.web5.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{boardNum}")
    public ResponseEntity<String> reportBoard(@PathVariable Integer boardNum, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        boolean success = reportService.createReport(boardNum, userDetails.getUsername());
        if (success) {
            return ResponseEntity.ok("게시글이 신고되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이미 신고한 게시글입니다.");
        }
    }

    @GetMapping("/admin/list")
    public String getReportList(Model model) {
        List<ReportEntity> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "admin/reportList";
    }

    @PostMapping("/admin/resolve/{reportId}")
    public String resolveReport(@PathVariable Integer reportId) {
        reportService.resolveReport(reportId);
        return "redirect:/report/admin/list";
    }

    @PostMapping("/admin/delete/{reportId}")
    public String deleteReport(@PathVariable Integer reportId) {
        reportService.deleteReport(reportId);
        return "redirect:/report/admin/list";
    }
}
