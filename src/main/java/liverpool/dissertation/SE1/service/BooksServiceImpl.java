package liverpool.dissertation.SE1.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import liverpool.dissertation.SE1.document.BookDocument;
import liverpool.dissertation.SE1.encryption.AES;
import liverpool.dissertation.SE1.entity.Book;
import liverpool.dissertation.SE1.repository.BooksDBRepository;
import liverpool.dissertation.SE1.repository.BooksSolrRepository;

@Service
public class BooksServiceImpl implements BooksService{
	
	
	@Autowired
	private BooksDBRepository booksDBRepository;
	
	@Autowired
	private BooksSolrRepository booksSolrRepository;
	
	@Value("${SE1.properties.encryptionKey}")
	private String encryptionKey;
	
	@Value("${SE1.properties.encryptionSalt}")
	private String encryptionSalt;

	@Override
	public List<Book> insertBooks(List<Book> books) {
		List<Book> insertedBooks = insertBooksInDBWithTitleEncrypted(books);
		int numberIndexed = indexBooks(insertedBooks);
		return insertedBooks;
	}
	
	private List<Book> insertBooksInDBWithTitleEncrypted(List<Book> books) {
		for(Book book : books) {
			Book encryptedBook = new Book();
			encryptedBook.setTitle(AES.encrypt(book.getTitle(), encryptionKey, encryptionSalt));
			Book insertedBook = booksDBRepository.save(encryptedBook);
			book.setId(insertedBook.getId());
		}
		return books;
	}
	
	private int indexBooks(List<Book> books) {
		int numberIndexed = 0;
		for(Book book : books) {
			String[] keywordsToIndex = book.getTitle().split(" ");
			StringBuilder encryptedTitle = new StringBuilder();
			for(String keywordToIndex : keywordsToIndex) {
				encryptedTitle.append(AES.encrypt(keywordToIndex.toLowerCase(), encryptionKey, encryptionSalt) + " ");
			}
			BookDocument bookDocument = new BookDocument();	
			bookDocument.setDatabaseId(AES.encrypt(new Long(book.getId()).toString(), encryptionKey, encryptionSalt));
			bookDocument.setTitle(encryptedTitle.toString());
			try {
				BookDocument inserted = booksSolrRepository.save(bookDocument);
				numberIndexed++;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return numberIndexed;
	}
	
	@Override
	public List<Book> findBooksByTitle(String title, int pageSize) {
		Page<BookDocument> page = booksSolrRepository.findByTitle(AES.encrypt(title.toLowerCase(), encryptionKey, encryptionSalt), PageRequest.of(0, pageSize));
		
		System.out.println("==================>>>>> " + page.getNumberOfElements());
		
		List<BookDocument> searchResult = page.getContent();
		List<Long> ids = new ArrayList<Long>();
		for(BookDocument document : searchResult) {
			Long id = new Long(AES.decrypt(document.getDatabaseId(), encryptionKey, encryptionSalt));
			ids.add(id);
		}
		List<Book> books = booksDBRepository.findAllById(ids);
		for(Book book : books) {
			book.setTitle(AES.decrypt(book.getTitle(), encryptionKey, encryptionSalt));
		}
		return books;
	}
}
