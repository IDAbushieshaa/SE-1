package liverpool.dissertation.SE1.service;

import java.util.List;

import liverpool.dissertation.SE1.entity.Book;


public interface BooksService {
	
	List<Book> insertBooks(List<Book> books);
	
	List<Book> findBooksByTitle(String title, int pageSize);

}
