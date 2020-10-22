package liverpool.dissertation.SE1.service;

import java.util.List;

import liverpool.dissertation.SE1.entity.Book;
import liverpool.dissertation.SE1.response.FindBooksResponse;


public interface BooksService {
	
	void insertBooks(List<Book> books);
	
	FindBooksResponse findBooksByTitle(String title, int pageSize);

}
