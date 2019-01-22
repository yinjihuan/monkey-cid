package org.monkey.cid.server.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.monkey.cid.server.base.Response;
import org.monkey.cid.server.base.ResponseData;
import org.monkey.cid.server.dto.ProjectDto;
import org.monkey.cid.server.param.AddProjectParam;
import org.monkey.cid.server.po.Project;
import org.monkey.cid.server.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cxytiandi.jdbc.util.PageBean;

@RestController
@RequestMapping("/rest")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/project/list")
	public ResponseData<PageBean<ProjectDto>> queryProjectList(int page) {
		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();
		int limit = 10;
		int start = PageBean.page2Start(page, limit);
		long total = projectService.queryProjectCount();
		List<Project> projectList = projectService.queryProjectList(start, limit);
		for (Project project : projectList) {
			ProjectDto dto = new ProjectDto();
			BeanUtils.copyProperties(project, dto);
			projectDtoList.add(dto);
		}
		PageBean<ProjectDto> data = new PageBean<ProjectDto>(start, limit, projectDtoList, total);
		return Response.ok(data);
	}
	
	@PostMapping("/project/add")
	public ResponseData<String> addProject(@RequestBody AddProjectParam param) {
		Project project = new Project();
		BeanUtils.copyProperties(param, project);
		Date addTime = new Date();
		project.setAddTime(addTime);
		project.setUpdateTime(addTime);
		Long id = projectService.addProject(project);
		return Response.ok(id.toString());
	}
}
