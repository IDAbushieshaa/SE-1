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

	@Query("Select distinct aw.books from AnalyzedWord aw join aw.books where aw.word = :word")
	Set<Book> findBooksByWord(String word);
	
	AnalyzedWord findByWord(String word);
}
