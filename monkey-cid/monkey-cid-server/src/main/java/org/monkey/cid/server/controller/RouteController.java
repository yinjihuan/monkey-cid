package org.monkey.cid.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

	@GetMapping("/index")
	public String indexPage() {
		return "/index/index";
	}
	
	@GetMapping("/project/list")
	public String projectListPage() {
		return "/project/projectList";
	}
	
}
