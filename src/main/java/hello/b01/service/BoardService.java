package hello.b01.service;

import hello.b01.dto.BoardDTO;
import hello.b01.dto.PageResponseDTO;
import hello.b01.dto.PageRequestDTO;

public interface BoardService {
    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}