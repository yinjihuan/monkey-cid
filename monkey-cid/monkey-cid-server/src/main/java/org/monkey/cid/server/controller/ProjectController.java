package org.monkey.cid.server.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.server.dto.ProjectDto;
import org.monkey.cid.server.param.AddProjectParam;
import org.monkey.cid.server.param.EditProjectBranchParam;
import org.monkey.cid.server.param.PublishProjectParam;
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
			dto.setId(project.getId().toString());
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
	
	@PostMapping("/project/edit")
	public ResponseData<String> editProject(@RequestBody AddProjectParam param) {
		Project project = new Project();
		BeanUtils.copyProperties(param, project);
		project.setId(Long.valueOf(param.getId()));
		Date updateTime = new Date();
		project.setUpdateTime(updateTime);
		projectService.updateProject(project);
		return Response.ok();
	}
	
	@PostMapping("/project/publish")
	public ResponseData<Boolean> publishProject(@RequestBody PublishProjectParam param) {
		projectService.publishProject(param);
		return Response.ok();
	}
	
	@GetMapping("/project/publish/logs")
	public ResponseData<List<String>> queryPublishLogs() {
		return Response.ok(projectService.queryPublishLogs());
	}
	
	@PostMapping("/project/branch")
	public ResponseData<Boolean> editProjectBranch(@RequestBody EditProjectBranchParam param) {
		projectService.updateBranch(param.getProjectId(), param.getBranchs());
		return Response.ok();
	}
}
