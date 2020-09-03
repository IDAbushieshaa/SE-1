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
	public AddBooksResponse addBooks(@RequestBody AddBooksCommand command) {
		List<Book> books = command.getBooks();
		List<Book> insertedBooks = booksService.insertBooks(books);
		AddBooksResponse response = new AddBooksResponse();
		return response;
	}
	
	
	@PostMapping(path = "/findBooksByTitle", consumes = "application/json", produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public  FindBooksResponse findBooks(@RequestBody FindBooksCommand command) {
		
		System.out.println("Received Request at " + new Date());
		
		Set<Book> result = booksService.findBooksByTitle(command.getSearchTerm(), 100);
		FindBooksResponse response = new FindBooksResponse();
		response.setBooks(result);
		return response;
	}
	
	public static void main(String[] args) {
		  

	}

}
