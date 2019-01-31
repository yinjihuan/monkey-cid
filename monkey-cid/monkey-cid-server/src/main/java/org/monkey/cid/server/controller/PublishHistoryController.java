package org.monkey.cid.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.server.dto.PublishHistoryDto;
import org.monkey.cid.server.po.Machine;
import org.monkey.cid.server.po.PublishHistory;
import org.monkey.cid.server.service.MachineService;
import org.monkey.cid.server.service.PublishHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cxytiandi.jdbc.util.DateUtils;
import com.cxytiandi.jdbc.util.PageBean;

@RestController
@RequestMapping("/rest")
public class PublishHistoryController {

	@Autowired
	private PublishHistoryService publishHistoryService;
	
	@Autowired
	private MachineService machineService;
	
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
	
	@GetMapping("/publishHistory/list")
	public ResponseData<PageBean<PublishHistoryDto>> queryPublishHistoryList(Long projectId, int page) {
		List<PublishHistoryDto> publishHistoryDtoList = new ArrayList<PublishHistoryDto>();
		int limit = 10;
		int start = PageBean.page2Start(page, limit);
		long total = publishHistoryService.queryPublishHistoryCount(projectId);
		List<PublishHistory> publishHistoryList = publishHistoryService.queryPublishHistoryList(projectId, start, limit);
		for (PublishHistory publishHistory : publishHistoryList) {
			PublishHistoryDto dto = new PublishHistoryDto();
			BeanUtils.copyProperties(publishHistory, dto);
			dto.setPublishId(publishHistory.getId().toString());
			dto.setPublishTimeText(DateUtils.date2Str(publishHistory.getPublishTime()));
			dto.setPublishResultText(publishHistory.getPublishResult() == 0 ? "成功" : "失败");
			String[] machines = publishHistory.getPublishMachines().split(",");
			List<String> publishMachineList = new ArrayList<>();
			for (String machineId : machines) {
				Machine machine = machineService.get(Long.parseLong(machineId));
				publishMachineList.add(machine.getIntranetIp());
			}
			dto.setPublishMachineList(publishMachineList);
			dto.setPublishMachineListCount(publishMachineList.size());
			publishHistoryDtoList.add(dto);
		}
		PageBean<PublishHistoryDto> data = new PageBean<PublishHistoryDto>(start, limit, publishHistoryDtoList, total);
		return Response.ok(data);
	}
	
}
