package liverpool.dissertation.SE1.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import liverpool.dissertation.SE1.encryption.AES;
import liverpool.dissertation.SE1.entity.AnalyzedWord;
import liverpool.dissertation.SE1.entity.Book;
import liverpool.dissertation.SE1.repository.AnalyzedWordsDBRepository;
import liverpool.dissertation.SE1.repository.BooksDBRepository;

@Service
public class BooksServiceImpl implements BooksService{
	
	
	@Autowired
	private BooksDBRepository booksDBRepository;
	
	@Autowired
	private AnalyzedWordsDBRepository analyzedWordsDBRepository;
	
	@Value("${SE1.properties.encryptionKey}")
	private String encryptionKey;
	
	@Value("${SE1.properties.encryptionSalt}")
	private String encryptionSalt;

	@Override
	public List<Book> insertBooks(List<Book> books) {
		List<Book> insertedBooks = new ArrayList<Book>();
		try {
			insertedBooks = insertBooksInDBWithTitleEncrypted(books);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return insertedBooks;
	}
	
	private List<Book> insertBooksInDBWithTitleEncrypted(List<Book> books) throws Exception{
		int i = 0;
		List<Book> booksToInsert = new ArrayList<Book>();
		for(Book book : books) {
			i++;
			Book encryptedBook = new Book();
			encryptedBook.setTitle(AES.encrypt(book.getTitle(), encryptionKey, encryptionSalt));
			List<AnalyzedWord> titleAnalyzed = this.analyzeText(book.getTitle());
			for(AnalyzedWord analyzedWord : titleAnalyzed) {
				AnalyzedWord found = analyzedWordsDBRepository.findByWord(analyzedWord.getWord());
				if(found == null) {
					AnalyzedWord newOne = analyzedWordsDBRepository.save(analyzedWord);
					encryptedBook.getAnalyzedTitle().add(newOne);
				} else {
					encryptedBook.getAnalyzedTitle().add(found);
				}
			}
			booksToInsert.add(encryptedBook);
			if(i%10 == 0 || booksToInsert.size() < 10) {
				System.out.println("Inserting the books: " + booksToInsert);
				booksDBRepository.saveAll(booksToInsert);
				Thread.sleep(500);
				booksToInsert = new ArrayList<Book>();
			}
		}
		return books;
	}
	

	
	private List<AnalyzedWord> analyzeText(String title) {
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		TokenStream stream = analyzer.tokenStream(null, title);
		stream = new EnglishMinimalStemFilter(stream);
		stream = new PorterStemFilter(stream);
		List<AnalyzedWord> analyzedWords = new ArrayList<>();
		
		try {
			stream.reset();
			while(stream.incrementToken()) {
				String token = stream.getAttribute(CharTermAttribute.class).toString();
				token = AES.encrypt(token, encryptionKey, encryptionSalt);
				AnalyzedWord analyzedWord = new AnalyzedWord();
				analyzedWord.setWord(token);
				analyzedWords.add(analyzedWord);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return analyzedWords;
	}
	
	@Override
	public Set<Book> findBooksByTitle(String title, int pageSize) {

		List<AnalyzedWord> analyzedWords = analyzeText(title);
		
		List<String> wordsToFind = new ArrayList<String>();
		Set<Book> booksFound = new HashSet<>();

		for(AnalyzedWord analyzedWord: analyzedWords) {
			wordsToFind.add(analyzedWord.getWord());
		}
		
		Set<Book> found = analyzedWordsDBRepository.findBooksByWord(wordsToFind);
		booksFound.addAll(found);
		
		for(Book book : booksFound) {
			book.setTitle(AES.decrypt(book.getTitle(), encryptionKey, encryptionSalt));
		}
		return booksFound;
	}

}
