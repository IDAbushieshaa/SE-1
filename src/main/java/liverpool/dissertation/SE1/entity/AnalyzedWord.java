package liverpool.dissertation.SE1.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;


@Entity
@Table(
	indexes = {@Index(name = "analyzed_word_index",  columnList="word", unique = true)}
)
public class AnalyzedWord {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length=300)
	private String word;
	
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="analyzedTitle")
	private Set<Book> books;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	public Set<Book> getBooks() {
		if(books == null)
			books = new HashSet<Book>();
		return books;
	}
	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	
	@Override
	public int hashCode() {
		return this.getWord().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof AnalyzedWord == false)
			return false;
		
		AnalyzedWord other = (AnalyzedWord) obj;
		
		if(other == null)
			return false;
		
		if(other.getWord() == null)
			return false;
			
		if(!other.getWord().equals(this.word))
			return false;
		
		return true;
	}
}
