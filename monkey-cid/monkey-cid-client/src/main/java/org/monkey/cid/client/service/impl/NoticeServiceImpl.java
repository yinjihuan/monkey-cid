package org.monkey.cid.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.monkey.cid.client.param.DeployParam;
import org.monkey.cid.client.service.NoticeService;
import org.monkey.cid.core.util.LinuxCommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


@Service
public class NoticeServiceImpl implements NoticeService {

	private Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);
	
	private ConcurrentHashMap<String, ArrayBlockingQueue<String>> logsQueueMap = new ConcurrentHashMap<>();
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean deployProject(DeployParam param) {
		//String deployScriptPath = "/home/goojia/deploy_fangjia_youfang_service.sh";
		try {
			String queueKey = param.getProjectName() + param.getBranch();
			if (!logsQueueMap.containsKey(queueKey)) {
				ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);
				logsQueueMap.put(queueKey, queue);
			}
			logger.info("deployProject: {}", param.getDeployScript());
			LinuxCommandUtil.exec(param.getDeployScript(), logsQueueMap.get(queueKey));
			
			// 检查是否部署成功，获取日志
			int startLine = 1;
			int endLine = 1;
			boolean health = false;
			for (int i = 0; i < 120; i++) {
				String cmd = "sed -n '%s,%sp' %s";
				endLine = getEndLine(param.getLogFile());
				String readLogCmd = String.format(cmd, startLine, endLine, param.getLogFile());
				logger.info("readLogCmd: {}", readLogCmd);
				String result = LinuxCommandUtil.exec(readLogCmd, logsQueueMap.get(queueKey));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startLine = endLine;
				if (checkProjectStatus(param.getAuthorization(), param.getHealthUrl(), result)) {
					health = true;
					break;
				}
			}
			return health;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkProjectStatus(String authorization, String healthUrl, String log) {
		try {
			String checkHealthUrl = "http://localhost:" + getPort(log) + healthUrl;
			logger.info("检查项目健康地址:{}", checkHealthUrl);
			logger.info("检查项目健康认证信息:{}", authorization);
			HttpHeaders requestHeaders = new HttpHeaders();
		    requestHeaders.add("Authorization", authorization);
		    HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
		    Map<String, String> result = restTemplate.exchange(checkHealthUrl, HttpMethod.GET, requestEntity, Map.class).getBody();
		    logger.info("检查项目健康状态:" + result.get("status"));
			String status = result.get("status");
			return status.equals("UP") ? true : false;
		} catch (Exception e) {
			logger.error("检查项目健康状态异常", e);
			return false;
		}
	}
	
	private int getEndLine(String logFile) throws Exception {
		String cmd = "wc -l " + logFile;
		String str = LinuxCommandUtil.exec(cmd, null);
		if(!StringUtils.hasText(str)) {
			return 1;
		}
		String endLine = str.split("\\s+")[0];
		if(!StringUtils.hasText(endLine)) {
			return 1;
		} 
		return Integer.parseInt(endLine) + 1;
	}

	@Override
	public List<String> queryDeployLogs(String projectName, String branch) {
		String queueKey = projectName + branch;
		List<String> logs = new ArrayList<String>();
		ArrayBlockingQueue<String> queue = logsQueueMap.get(queueKey);
		if (queue == null) return new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			String log = queue.poll();
			if (StringUtils.hasText(log)) {
				logs.add(log);
			}
		}
		return logs;
	}
	
	private int getPort(String log) {
		Pattern p = Pattern.compile("port\\(s\\)(.*?)\\(http\\)");
		Matcher m = p.matcher(log);
		if (m.find()) {
			return Integer.parseInt(m.group(1).trim());
		}
		return 8080;
	}

}
