package liverpool.dissertation.SE1.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import liverpool.dissertation.SE1.repository.BooksSolrRepository;

@Configuration
@EnableSolrRepositories(basePackageClasses = BooksSolrRepository.class)
@ComponentScan
public class SolrConfig {

	private String solrUrl;
	
    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr/").build();
    }

    @Bean
    public SolrTemplate getSolrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }
}
