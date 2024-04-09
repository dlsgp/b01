package hello.b01.controller;

import hello.b01.dto.ReplyDTO;
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
@Tag(name="replies", description = "댓글 관련 API")
//@RequiredArgsConstructor
public class ReplyController {


    @Tag(name = "Replies POST", description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "댓글을 등록한다.")
    public Map<String, Long> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult) throws BindException {

        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", 111L);

        return resultMap;
    }
}