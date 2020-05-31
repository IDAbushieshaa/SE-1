package liverpool.dissertation.SE1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE1.document.BookDocument;


@Repository
public interface BooksSolrRepository extends SolrCrudRepository<BookDocument, Integer>{
	
	BookDocument findBySolrId(Integer id);
	
	@Query("TITLE:*?0*")
	Page<BookDocument> findByTitle(String searchTerm, Pageable pageable);
	
	@Query("DB_ID:*?0*")
	Page<BookDocument> findByDatabaseId(String searchTerm, Pageable pageable);

}