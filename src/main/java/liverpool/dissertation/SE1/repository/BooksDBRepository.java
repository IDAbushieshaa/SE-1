package liverpool.dissertation.SE1.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE1.entity.Book;


@Repository
public interface BooksDBRepository extends JpaRepository<Book, Long>{
		
	@Query("SELECT b FROM AnalyzedWord aw, Book b where aw.word = :word")
	Set<Book> findBooksByAnalyzedWord(String word);

}
