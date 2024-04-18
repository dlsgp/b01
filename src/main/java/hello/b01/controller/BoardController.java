package hello.b01.controller;

import hello.b01.dto.*;
import hello.b01.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
@Tag(name="board", description = "게시물 관련 API")
public class BoardController {

    @Value("${hello.upload.path}")   //import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    private final BoardService boardService;

//    @GetMapping("/list")
//    @Operation(summary = "전체 게시물 목록을 조회 한다.")
//    public void list(PageRequestDTO pageRequestDTO, Model model) {
//
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
//
//        log.info(responseDTO);
//
//        model.addAttribute("responseDTO", responseDTO);
//    }



    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

//        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public void registerGET() {
    }


    @PostMapping("/register")// localhost:8080/board/register
    @Operation(summary ="새로운 게시물을 추가한다.")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("board POST register.........");

        if (bindingResult.hasErrors()) {
            log.info("has errors.........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(@RequestParam("bno") Long bno, PageRequestDTO pageRequestDTO, Model model) {

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/modify")
    @Operation(summary = "게시물을 수정 합니다.")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("board modify post........." + boardDTO);

        if (bindingResult.hasErrors()) {
            log.info("has errors.........");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    @Operation(summary = "게시물 하나를 삭제 합니다.")
    public String remove(@RequestParam("bno") BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardDTO.getBno();
        log.info("remove post.. " + bno);

        boardService.remove(bno);

        // 게시물이 데이터베이스상에서 삭제되었다면 첨부파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0) {
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }

    public void removeFiles(List<String> files){
        for(String fileName:files){
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                // 섬네일이 존재한다면
                if(contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                    thumbnailFile.delete();
                }

            }catch (Exception e){
                log.error(e.getMessage());
            }

        }//end for
    }


}
