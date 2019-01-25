package org.monkey.cid.client.controller;

import org.monkey.cid.client.service.NoticeService;
import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@PostMapping("/notice/deploy")
	public ResponseData<Boolean> deployProject() {
		boolean result = noticeService.deployProject();
		return Response.ok(result);
	}
	
}
