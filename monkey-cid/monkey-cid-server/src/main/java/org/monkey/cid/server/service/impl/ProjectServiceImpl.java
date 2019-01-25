package org.monkey.cid.server.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.monkey.cid.core.base.ResponseCode;
import org.monkey.cid.core.base.ResponseData;
import org.monkey.cid.core.exception.GlobalException;
import org.monkey.cid.core.exception.ServerException;
import org.monkey.cid.core.util.LinuxCommandUtil;
import org.monkey.cid.server.param.PublishProjectParam;
import org.monkey.cid.server.po.Machine;
import org.monkey.cid.server.po.Project;
import org.monkey.cid.server.service.MachineService;
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
import com.cxytiandi.jdbc.util.DateUtils;

@Service
public class ProjectServiceImpl extends EntityService<Project> implements ProjectService {

	private Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	private ConcurrentHashMap<String, ArrayBlockingQueue<String>> logsQueueMap = new ConcurrentHashMap<>();
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MachineService machineService;
	
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
	public void publishProject(PublishProjectParam param) {
		Project project = this.getById("id", param.getProjectId());
		param.setBuildScript(project.getBuildScript());
		param.setGitUrl(project.getGitUrl());
		this.buildProject(param);
		this.deployProject();
	}
	
	private void deployProject() {
		ResponseData resp = restTemplate.postForObject("http://192.168.0.222:3101/notice/deploy", null, ResponseData.class);
		System.out.println("deployProject:" + resp.getCode());  
	}
	
	private void buildProject(PublishProjectParam param) {
		//String command = "/home/goojia/build/build_fangjia_youfang_service.sh master";
		StringBuilder command = new StringBuilder();
		command.append(param.getBuildScript());
		command.append(" ");
		command.append(param.getPublishProjectName());
		command.append(" ");
		command.append(param.getPublishBranch());
		command.append(" ");
		command.append(param.getGitUrl());
		logger.info("开始执行部署脚本 {}", command.toString());
		
		String queueKey = param.getPublishProjectName() + param.getPublishBranch();
		if (!logsQueueMap.containsKey(queueKey)) {
			ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);
			logsQueueMap.put(queueKey, queue);
		}
	
		try {
			LinuxCommandUtil.exec(command.toString(), logsQueueMap.get(queueKey));
			param.getPublishMachineList().forEach(id -> {
				Machine machine = machineService.get(Long.parseLong(id));
				if (machine == null) {
					throw new GlobalException("机器不存在：" + id, ResponseCode.SERVER_ERROR_CODE);
				}
				doBuildProject(machine, param);
			});
		} catch (Exception e) {
			logger.error("项目部署异常", e);
			throw new ServerException(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void doBuildProject(Machine machine, PublishProjectParam param) {
		//String jarPath = "/home/goojia/build/viewstore/master.fangjia-youfang-service/fangjia-youfang-service/fangjia-youfang-service/target/fangjia-youfang-service-1.0.0.jar";
		//String fileFolder = "/home/goojia/publish/master/fangjia-youfang-service/2019-12-12";
		//String fileName = "fangjia-youfang-service-1.0.0.jar";
		String fileFolder = getProjectPublishCatalog(machine.getPublishCatalog(), param.getPublishBranch(), param.getPublishProjectName());
		String jarPath = param.getJarPath();
		String fileName = jarPath.substring(jarPath.lastIndexOf("/") + 1, jarPath.length());
		
		FileSystemResource resource = new FileSystemResource(new File(jarPath));  
		MultiValueMap<String, Object> uploadParam = new LinkedMultiValueMap<>();  
		uploadParam.add("file", resource);  
		uploadParam.add("fileFolder", fileFolder);  
		uploadParam.add("fileName", fileName);
		
		logger.info("client url: {}", machine.getClientUrl());
		logger.info("jarPath： {}", jarPath);
		logger.info("fileName： {}", fileName);
		logger.info("fileFolder： {}", fileFolder);
		ResponseData resp = restTemplate.postForObject(machine.getClientUrl(), uploadParam, ResponseData.class);
		if (resp.getCode() != 200) {
			throw new ServerException(resp.getMessage());
		}
		logger.info("上传成功");
	}
	
	private String getProjectPublishCatalog(String publishCatalog, String branch, String projectName) {
		StringBuilder path = new StringBuilder();
		path.append(publishCatalog);
		path.append("/");
		path.append(branch);
		path.append("/");
		path.append(projectName);
		path.append("/");
		String time = DateUtils.date2Str(new Date(), "yyyy-MM-dd.HH:mm:ss");
		path.append(time);
		return path.toString();
	}
	
	@Override
	public List<String> queryPublishLogs() {
		List<String> logs = new ArrayList<String>();
		ArrayBlockingQueue<String> queue = logsQueueMap.get("yinjihuan");
		if (queue == null) return new ArrayList<>();
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
