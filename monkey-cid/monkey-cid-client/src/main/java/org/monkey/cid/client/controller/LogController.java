package org.monkey.cid.client.controller;

import java.util.List;

import org.monkey.cid.client.service.NoticeService;
import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

	@Autowired
	private NoticeService noticeService;
	
	@GetMapping("/logs")
	public ResponseData<List<String>> queryDeployLogs(String projectName, String branch) {
		List<String> logs = noticeService.queryDeployLogs(projectName, branch);
		return Response.ok(logs);
	}
	
}
