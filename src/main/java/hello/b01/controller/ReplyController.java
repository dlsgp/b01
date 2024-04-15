package hello.b01.controller;

import hello.b01.dto.PageRequestDTO;
import hello.b01.dto.PageResponseDTO;
import hello.b01.dto.ReplyDTO;
import hello.b01.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
@Tag(name="reply-controller", description = "Reply Controller")

public class ReplyController {

    private final ReplyService replyService;



    //    @Tag(name = "reply-controller", description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Replies POST")
    public Map<String, Long> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult) throws BindException {

        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;

    }

    //    @Tag(name = "reply-controller", description = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value ="/list/{bno}")
    @Operation(summary = "Replies of Board")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }


    @GetMapping("/{rno}")
    @Operation(summary = "Read Reply")
    public ReplyDTO getReplyDTO( @PathVariable("rno") Long rno){
        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }

    @DeleteMapping("/{rno}")
    @Operation(summary = "Delete Reply")
    public Map<String,Long> remove( @PathVariable("rno") Long rno){
        replyService.remove(rno) ;

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modify Reply")
    public Map<String,Long> remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

}