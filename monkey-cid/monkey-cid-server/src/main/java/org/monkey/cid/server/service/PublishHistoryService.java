package org.monkey.cid.server.service;

import org.monkey.cid.server.po.PublishHistory;

public interface PublishHistoryService {

	void save(PublishHistory history);
	
	/**
	 * 获取项目最新一次的发布历史信息
	 * @param projectId
	 * @return
	 */
	PublishHistory getLatelyPublishHistory(Long projectId);
}
