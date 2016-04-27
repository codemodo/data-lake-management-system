package project.components;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SearchEngine {
	
	@Cacheable("testCache")
	public int doSearch() throws InterruptedException {
		Thread.sleep(4000);
		return 1;
	}

}
