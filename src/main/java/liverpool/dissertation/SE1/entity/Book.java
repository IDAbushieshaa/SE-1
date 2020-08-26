package liverpool.dissertation.SE1.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String title;
	
	@JsonIgnore
	private String titleAnalyzed;

	
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
	
	public String getTitleAnalyzed() {
		return titleAnalyzed;
	}
	public void setTitleAnalyzed(String titleAnalyzed) {
		this.titleAnalyzed = titleAnalyzed;
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
