package at.ac.univie.rscm.spring.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {
	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("redirect:" + "/index.jsp");
	}

}
