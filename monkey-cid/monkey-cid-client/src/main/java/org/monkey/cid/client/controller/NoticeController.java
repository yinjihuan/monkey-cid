package org.monkey.cid.client.controller;

import org.monkey.cid.client.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@PostMapping("/notice/deploy")
	public String deployProject() {
		noticeService.deployProject();
		return "success";
	}
	
}
