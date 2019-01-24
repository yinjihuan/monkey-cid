package org.monkey.cid.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.monkey.cid.core.util.LinuxCommandUtil;
import org.monkey.cid.server.po.Project;
import org.monkey.cid.server.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cxytiandi.jdbc.EntityService;

@Service
public class ProjectServiceImpl extends EntityService<Project> implements ProjectService {

	private Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	private ConcurrentHashMap<String, ArrayBlockingQueue> logsMap = new ConcurrentHashMap<>();
	
	public List<Project> queryProjectList(int start, int limit) {
		return super.listForPage(start, limit);
	}

	public long queryProjectCount() {
		return super.count();
	}

	@Override
	public Long addProject(Project project) {
		return (Long) super.save(project);
	}

	@Override
	public void updateProject(Project project) {
		super.updateByContainsFields(project, "id", new String[] { "name", "chinese_name", "git_url", "update_time" });
	}

	@Async
	@Override
	public void publishProject(Project project) {
		this.buildProject();
	}
	
	private void buildProject() {
		String command = "/home/goojia/build/build_fangjia_youfang_service.sh master";
		logger.info("开始执行部署脚本 {}", command);
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);
		logsMap.put("yinjihuan", queue);
		try {
			LinuxCommandUtil.exec(command, queue);
		} catch (Exception e) {
			logger.error("项目部署异常", e);
		}
	}

	@Override
	public List<String> queryPublishLogs() {
		List<String> logs = new ArrayList<String>();
		ArrayBlockingQueue<String> queue = logsMap.get("yinjihuan");
		for (int i = 0; i < 100; i++) {
			String log = queue.poll();
			if (StringUtils.hasText(log)) {
				logs.add(queue.poll());
			}
		}
		return logs;
	}

	@Override
	public void updateBranch(String id, String branchs) {
		super.updateByContainsFields(new String[] { "branchs" }, "id", new Object[] { branchs, id });
	}

}
