package liverpool.dissertation.SE1.command;

import java.util.ArrayList;
import java.util.List;

import liverpool.dissertation.SE1.entity.Book;

public class AddBooksCommand {
	
	private List<Book> books = new ArrayList<>();

	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
}