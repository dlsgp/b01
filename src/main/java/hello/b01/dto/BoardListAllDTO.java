package hello.b01.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
//@Getter
//@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {

    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private Long replyCount;

    @Column(length = 100)
    private List<BoardImageDTO> boardImages;
}
