package project.webComponents;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import project.components.SearchEngine;

@Controller
public class SearchController {
	
	@Autowired
	@Qualifier("searchEngineBean")
	SearchEngine searchEngine;
	
	@RequestMapping("/search")
	public String home(@RequestParam(value="query", required=true) String query, Model model, HttpSession session) {
		model.addAttribute("isLoggedIn", SessionHelperFunctions.isLoggedIn(session));
		try {
			searchEngine.doSearch();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "results";
	}

}
