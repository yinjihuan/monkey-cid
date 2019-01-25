package org.monkey.cid.server.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.core.util.LinuxCommandUtil;
import org.monkey.cid.server.po.Project;
import org.monkey.cid.server.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.cxytiandi.jdbc.EntityService;

@Service
public class ProjectServiceImpl extends EntityService<Project> implements ProjectService {

	private Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	private ConcurrentHashMap<String, ArrayBlockingQueue> logsMap = new ConcurrentHashMap<>();
	
	@Autowired
	private RestTemplate restTemplate;
	
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
			String jarPath = "/home/goojia/build/viewstore/master.fangjia-youfang-service/fangjia-youfang-service/fangjia-youfang-service/target/fangjia-youfang-service-1.0.0.jar";
			String fileFolder = "/home/goojia/publish/master/fangjia-youfang-service/2019-12-12";
			String fileName = "fangjia-youfang-service-1.0.0.jar";
			FileSystemResource resource = new FileSystemResource(new File(jarPath));  
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();  
			param.add("file", resource);  
			param.add("fileFolder", fileFolder);  
			param.add("fileName", fileName); 
			ResponseData resp = restTemplate.postForObject("http://192.168.0.222:3101/file/upload", param, ResponseData.class);  
			System.out.println(resp.getCode());  
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
