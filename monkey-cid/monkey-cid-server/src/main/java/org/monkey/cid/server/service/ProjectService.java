package org.monkey.cid.server.service;

import java.util.List;

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
	
}
