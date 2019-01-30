package org.monkey.cid.server.service;

import java.util.List;

import org.monkey.cid.server.dto.PublishProjectDto;
import org.monkey.cid.server.param.PublishProjectParam;
import org.monkey.cid.server.po.Project;

public interface ProjectService {

	/**
	 * 查询项目列表
	 * @param start
	 * @param limit
	 * @return
	 */
	List<Project> queryProjectList(int start, int limit);
	
	/**
	 * 查询项目总数量
	 * @return
	 */
	long queryProjectCount();
	
	/**
	 * 新增项目
	 * @param project
	 * @return
	 */
	Long addProject(Project project);
	
	/**
	 * 修改项目
	 * @param project
	 */
	void updateProject(Project project);
	
	void updateBranch(String id, String branchs);
	
	PublishProjectDto publishProject(PublishProjectParam param);
	
	List<String> queryPublishLogs();
	
}
