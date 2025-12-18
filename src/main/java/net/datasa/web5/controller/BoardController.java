package net.datasa.web5.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.dto.UploadFile;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.BoardFileEntity;
import net.datasa.web5.security.CustomUserDetails;
import net.datasa.web5.service.BoardService;
import net.datasa.web5.util.FileService;

@Controller
@RequestMapping("board")
public class BoardController {

    @Value("${file.upload.path}")
    private String uploadPath;

    private final BoardService boardService;
    private final FileService fileService;

    public BoardController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @PostMapping("upload")
    @ResponseBody
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String savedFileName = fileService.saveFile(file, uploadPath);
        String url = "/board/media/" + savedFileName;
        
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return response;
    }

    @GetMapping("media/{filename}")
    public ResponseEntity<Resource> getMedia(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("list")
    public String list(@RequestParam(value = "type", defaultValue = "title_content") String type,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        Page<BoardDTO> boardPage;
        if (keyword != null && !keyword.isBlank()) {
            boardPage = boardService.search(type, keyword, pageable);
            model.addAttribute("keyword", keyword);
            model.addAttribute("type", type);
        } else {
            boardPage = boardService.findAll(pageable);
        }
        model.addAttribute("boardPage", boardPage);
        return "board/list";
    }

    // 게시글 상세보기
    @GetMapping("view/{boardNum}")
    public String view(@PathVariable("boardNum") Integer boardNum, Model model) {
        BoardEntity boardEntity = boardService.findById(boardNum);
        BoardDTO board = BoardDTO.toDTO(boardEntity, boardEntity.getComments().size());
        model.addAttribute("board", board);
        return "board/view";
    }

    // 글쓰기 폼
    @GetMapping("write")
    public String writeForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            // TODO: RedirectAttributes를 사용하여 사용자에게 메시지 전달
            return "redirect:/board/list";
        }
        model.addAttribute("board", new BoardDTO());
        return "board/write";
    }

    // 글쓰기 처리
    @PostMapping("/write")
    public String write(@RequestParam("boardTitle") String boardTitle,
                        @RequestParam("boardContent") String boardContent,
                        @RequestParam("uploadFiles") MultipartFile[] files,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            // TODO: RedirectAttributes를 사용하여 사용자에게 메시지 전달
            return "redirect:/board/list";
        }
        BoardDTO board = BoardDTO.builder().boardTitle(boardTitle).boardContent(boardContent).build();
        boardService.save(board.toEntity(), files, userDetails.getMember());
        return "redirect:/board/list";
    }

    // 게시글 삭제




    @PostMapping("delete/{boardNum}")
    public String delete(@PathVariable("boardNum") Integer boardNum) {
        boardService.delete(boardNum);
        return "redirect:/board/list";
    }

    // 게시글 수정 폼
    @GetMapping("edit/{boardNum}")
    public String editForm(@PathVariable("boardNum") Integer boardNum, Model model) {
        BoardEntity boardEntity = boardService.findById(boardNum);
        BoardDTO board = BoardDTO.toDTO(boardEntity, boardEntity.getComments().size());
        model.addAttribute("board", board);
        return "board/edit";
    }

    // 게시글 수정 처리
    @PostMapping("edit/{boardNum}")
    public String edit(@PathVariable("boardNum") final Integer boardNum, // final 키워드 추가
                       @RequestParam("boardTitle") String boardTitle,
                       @RequestParam("boardContent") String boardContent,
                       @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds,
                       @RequestParam("uploadFiles") MultipartFile[] files) {

        boardService.update(boardNum, boardTitle, boardContent, deleteFileIds, files);
        return "redirect:/board/view/" + boardNum;
    }

    // 파일 다운로드
    @GetMapping("download/{fileNum}")
    public ResponseEntity<Resource> download(@PathVariable("fileNum") Integer fileNum) throws MalformedURLException {
        BoardFileEntity file = boardService.findFileByFileNum(fileNum);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(uploadPath).resolve(file.getSavedName()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalName() + "\"")
                .body(resource);
    }

}
