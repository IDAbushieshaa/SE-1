package liverpool.dissertation.SE1.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "BOOKS-SE-1")
public class BookDocument {
	
	@Id
	@Indexed(name="id", type="string")
	private String solrId;
	
	
	@Indexed(name="TITLE", type="string")
	private String title;
	
	
	@Field(value="DB_ID")
	private String databaseId;


	public String getSolrId() {
		return solrId;
	}
	public void setSolrId(String solrId) {
		this.solrId = solrId;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	
	
	@Override
	public String toString() {
		return "Database ID = " + databaseId + " & solrId = " + solrId;
	}
}
