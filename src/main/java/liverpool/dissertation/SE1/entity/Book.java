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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length=1000)
	private String title;
	
	@JsonIgnore
	@Column(length=300)
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name="ANALYZED_TITLE",
        joinColumns = {@JoinColumn(name="book_id")},
        inverseJoinColumns = {@JoinColumn(name="analyzed_word_id")}
    )
	private Set<AnalyzedWord> analyzedTitle;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Set<AnalyzedWord> getAnalyzedTitle() {
		if(analyzedTitle == null)
			analyzedTitle = new HashSet<AnalyzedWord>();
		return analyzedTitle;
	}
	public void setAnalyzedTitle(Set<AnalyzedWord> analyzedTitle) {
		this.analyzedTitle = analyzedTitle;
	}
	
	@Override
	public String toString() {
		return "ID = \t" + id +  " & Title = \t" + title;
	}
	
	@Override
	public int hashCode() {
		Long hashCodeLong = this.id % Integer.MAX_VALUE;
		int hashCodeInt = new Long(hashCodeLong).intValue();
		return hashCodeInt;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Book == false)
			return false;
		
		Book book = (Book) obj;
		if(this.id == 0 || book.getId() == 0)
			return false;
		
		if(this.id != book.getId())
			return false;
		
		return true;
	}
}
