package hello.b01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;

    @Column
    private String fileName;

    @Column
    private int ord;

    @ManyToOne
    private Board board;

    @Override
    public int compareTo(BoardImage other){
        return this.ord - other.ord;
    }

    public void changeBoard(Board board){
        this.board = board;
    }


}
