package net.datasa.web5.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.NoticeDTO;
import net.datasa.web5.domain.entity.NoticeEntity;
import net.datasa.web5.security.CustomUserDetails;
import net.datasa.web5.service.NoticeService;

@Controller
@RequestMapping("notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("list")
    public String list(@RequestParam(value = "type", defaultValue = "title_content") String type,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        Page<NoticeDTO> noticePage;
        if (keyword != null && !keyword.isBlank()) {
            noticePage = noticeService.search(type, keyword, pageable);
            model.addAttribute("keyword", keyword);
            model.addAttribute("type", type);
        } else {
            noticePage = noticeService.findAll(pageable);
        }
        model.addAttribute("noticePage", noticePage);
        return "notice/list";
    }

    @GetMapping("view/{noticeNum}")
    public String view(@PathVariable("noticeNum") Integer noticeNum, Model model) {
        NoticeEntity notice = noticeService.findById(noticeNum);
        model.addAttribute("notice", notice);
        return "notice/view";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("write")
    public String writeForm(Model model) {
        model.addAttribute("notice", new NoticeEntity());
        return "notice/write";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/write")
    public String write(@ModelAttribute NoticeEntity notice, @AuthenticationPrincipal CustomUserDetails userDetails) {
        noticeService.save(notice, userDetails.getMember());
        return "redirect:/notice/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("delete/{noticeNum}")
    public String delete(@PathVariable("noticeNum") Integer noticeNum) {
        noticeService.delete(noticeNum);
        return "redirect:/notice/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("edit/{noticeNum}")
    public String editForm(@PathVariable("noticeNum") Integer noticeNum, Model model) {
        NoticeEntity notice = noticeService.findById(noticeNum);
        model.addAttribute("notice", notice);
        return "notice/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("edit/{noticeNum}")
    public String edit(@PathVariable("noticeNum") Integer noticeNum,
                       @RequestParam("noticeTitle") String noticeTitle,
                       @RequestParam("noticeContent") String noticeContent) {
        noticeService.update(noticeNum, noticeTitle, noticeContent);
        return "redirect:/notice/view/" + noticeNum;
    }
}
