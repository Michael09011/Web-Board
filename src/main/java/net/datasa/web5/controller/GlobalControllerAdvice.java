package net.datasa.web5.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.dto.NoticeDTO;
import net.datasa.web5.service.BoardService;
import net.datasa.web5.service.NoticeService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final BoardService boardService;
    private final NoticeService noticeService;

    @ModelAttribute("boardList")
    public List<BoardDTO> addBoardList() {
        Pageable pageable = PageRequest.of(0, 8, Sort.by("createdDate").descending());
        Page<BoardDTO> boardPage = boardService.findAll(pageable);
        return boardPage.getContent();
    }

    @ModelAttribute("noticeList")
    public List<NoticeDTO> addNoticeList() {
        return noticeService.findTop5();
    }

    @ModelAttribute("currentDate")
    public LocalDateTime addCurrentDate() {
        return LocalDateTime.now();
    }
}
