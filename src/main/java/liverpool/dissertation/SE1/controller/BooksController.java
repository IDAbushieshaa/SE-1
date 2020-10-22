package liverpool.dissertation.SE1.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import liverpool.dissertation.SE1.command.AddBooksCommand;
import liverpool.dissertation.SE1.command.FindBooksCommand;
import liverpool.dissertation.SE1.entity.Book;
import liverpool.dissertation.SE1.response.AddBooksResponse;
import liverpool.dissertation.SE1.response.FindBooksResponse;
import liverpool.dissertation.SE1.service.BooksService;

@RestController
@RequestMapping(path="/books")
public class BooksController {
	
	@Autowired
	BooksService booksService;
	
	@PostMapping(path= "/addBooks", consumes = "application/json", produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public void addBooks(@RequestBody AddBooksCommand command) {
		List<Book> books = command.getBooks();
		System.out.println("Books Received = " + books);
		booksService.insertBooks(books);
	}
	
	@PostMapping(path = "/findBooksByTitle", consumes = "application/json", produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public  FindBooksResponse findBooks(@RequestBody FindBooksCommand command) {
		Date date = new Date();
		System.out.println(date.getTime());
		FindBooksResponse response = booksService.findBooksByTitle(command.getSearchTerm(), 100);
		return response;
	}
}
