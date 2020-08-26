package liverpool.dissertation.SE1.response;

import java.util.HashSet;
import java.util.Set;

import liverpool.dissertation.SE1.entity.Book;

public class FindBooksResponse {
	
	
	private Set<Book> books = new HashSet<>();

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
}
