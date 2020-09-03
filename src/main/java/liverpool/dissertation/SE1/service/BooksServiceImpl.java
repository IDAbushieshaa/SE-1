package liverpool.dissertation.SE1.service;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.FileDictionary;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import liverpool.dissertation.SE1.encryption.AES;
import liverpool.dissertation.SE1.entity.Book;
import liverpool.dissertation.SE1.repository.BooksDBRepository;

@Service
public class BooksServiceImpl implements BooksService{
	
	
	@Autowired
	private BooksDBRepository booksDBRepository;
	
	@Value("${SE1.properties.encryptionKey}")
	private String encryptionKey;
	
	@Value("${SE1.properties.encryptionSalt}")
	private String encryptionSalt;

	@Override
	public List<Book> insertBooks(List<Book> books) {

		List<Book> insertedBooks = insertBooksInDBWithTitleEncrypted(books);

		return insertedBooks;
	}
	
	private List<Book> insertBooksInDBWithTitleEncrypted(List<Book> books) {
		for(Book book : books) {
			Book encryptedBook = new Book();
			encryptedBook.setTitle(AES.encrypt(book.getTitle(), encryptionKey, encryptionSalt));
			String titleAnalyzed = this.analyzeText(book.getTitle());
			encryptedBook.setTitleAnalyzed(titleAnalyzed);
			Book insertedBook = booksDBRepository.save(encryptedBook);
			book.setId(insertedBook.getId());
		}
		return books;
	}
	

	
	private String analyzeText(String title) {
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		TokenStream stream = analyzer.tokenStream(null, title);
		stream = new EnglishMinimalStemFilter(stream);
		stream = new PorterStemFilter(stream);
		List<String> analyzedTitle = new ArrayList<>();
		
		try {
			stream.reset();
			while(stream.incrementToken()) {
				analyzedTitle.add(stream.getAttribute(CharTermAttribute.class).toString());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		String encryptedAnalyzedTitle = "";
		for(String token : analyzedTitle) {
			encryptedAnalyzedTitle += AES.encrypt(token, encryptionKey, encryptionSalt);
			encryptedAnalyzedTitle += " ";
		}

		try {
			analyzer.close();
			stream.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return encryptedAnalyzedTitle;
	}
	
	@Override
	public Set<Book> findBooksByTitle(String title, int pageSize) {

		String encryptedAnalyzedTitle = analyzeText(title);
		
		String[] analyzedTitleComponents = encryptedAnalyzedTitle.split(" ");
		
		Set<Book> booksFound = new HashSet<>();
		for(String analyzedTitleComponet : analyzedTitleComponents) {
			Set<Book> books = booksDBRepository.findByEncryptedAnalyzedTitle(analyzedTitleComponet.trim());
			booksFound.addAll(books);
		}
		
		for(Book book : booksFound) {
			book.setTitle(AES.decrypt(book.getTitle(), encryptionKey, encryptionSalt));
		}
		return booksFound;
	}
	
	
	public static void main(String[] args) {
		try {
			  SpellChecker spellchecker = new SpellChecker(new MMapDirectory(Paths.get("./index")));
			  spellchecker.clearIndex();
			  spellchecker.indexDictionary(new FileDictionary(new FileInputStream("./WordsToIndex.txt")), new IndexWriterConfig(), true);
			  String[] suggestions = spellchecker.suggestSimilar(" Daowod", 5, 0.1F);
			  for(String string : suggestions) {
				  System.out.println("==> " + string);
			  }
		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}
}
