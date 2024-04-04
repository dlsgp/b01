package hello.b01.repository;

import hello.b01.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hello.b01.repository.search.BoardSearch;
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

}
