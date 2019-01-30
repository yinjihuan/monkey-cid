package org.monkey.cid.server.controller;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.server.dto.PublishHistoryDto;
import org.monkey.cid.server.po.PublishHistory;
import org.monkey.cid.server.service.PublishHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class PublishHistoryController {

	@Autowired
	private PublishHistoryService publishHistoryService;
	
	/**
	 * 获取项目最近的发布历史
	 * @param projectId
	 * @return
	 */
	@GetMapping("/publishHistory/lately")
	public ResponseData<PublishHistoryDto> getLatelyPublishHistory(String projectId) {
		PublishHistory publishHistory = publishHistoryService.getLatelyPublishHistory(Long.parseLong(projectId));
		if (publishHistory == null) {
			return Response.ok();
		}
		PublishHistoryDto data = new PublishHistoryDto();
		BeanUtils.copyProperties(publishHistory, data);
		return Response.ok(data);
	}
	
}
