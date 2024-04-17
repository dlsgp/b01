package hello.b01.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO {

    private String uuid;

    @Column(length = 100)
    private String fileName;

    private int ord;

}


