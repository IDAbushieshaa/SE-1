package liverpool.dissertation.SE1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public AddBooksResponse addBooks(@RequestBody AddBooksCommand command) {
		
		List<Book> books = command.getBooks();
		
		List<Book> insertedBooks = booksService.insertBooks(books);
		
		AddBooksResponse response = new AddBooksResponse();
		response.setSuccess(true);
		response.setStatus("200");
		return response;
	}
	
	
	@PostMapping(path = "/findBooksByTitle", consumes = "application/json", produces = "application/json")
	public  FindBooksResponse findBooks(@RequestBody FindBooksCommand command) {
		List<Book> result = booksService.findBooksByTitle(command.getSearchTerm(), 100);
		FindBooksResponse response = new FindBooksResponse();
		response.setBooks(result);
		response.setStatus("200");
		response.setSuccess(true);
		return response;
	}

}
