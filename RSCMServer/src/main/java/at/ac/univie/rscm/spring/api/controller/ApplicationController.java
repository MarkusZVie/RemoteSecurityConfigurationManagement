package at.ac.univie.rscm.spring.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/")
public class ApplicationController {
	@GetMapping("/process")
	public String process() {
		return "processing .... ";
	}
}
