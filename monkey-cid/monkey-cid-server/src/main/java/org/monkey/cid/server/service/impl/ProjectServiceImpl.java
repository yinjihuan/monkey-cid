package org.monkey.cid.server.service.impl;

import java.util.List;

import org.monkey.cid.server.po.Project;
import org.monkey.cid.server.service.ProjectService;
import org.springframework.stereotype.Service;
import com.cxytiandi.jdbc.EntityService;

@Service
public class ProjectServiceImpl extends EntityService<Project> implements ProjectService {

	public List<Project> queryProjectList(int start, int limit) {
		return super.listForPage(start, limit);
	}

	public long queryProjectCount() {
		return super.count();
	}

}
