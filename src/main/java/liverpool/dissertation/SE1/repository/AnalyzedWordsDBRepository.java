package liverpool.dissertation.SE1.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE1.entity.AnalyzedWord;
import liverpool.dissertation.SE1.entity.Book;

@Repository
public interface AnalyzedWordsDBRepository extends JpaRepository<AnalyzedWord, Long>{

	@Query("Select aw.books from AnalyzedWord aw where aw.word IN :wordsList")
	Set<Book> findBooksByWord(List wordsList);
	
	AnalyzedWord findByWord(String word);
}
