package org.monkey.cid.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.monkey.cid.client.param.DeployParam;
import org.monkey.cid.client.service.NoticeService;
import org.monkey.cid.core.util.LinuxCommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NoticeServiceImpl implements NoticeService {

	private Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);
	
	private ConcurrentHashMap<String, ArrayBlockingQueue<String>> logsQueueMap = new ConcurrentHashMap<>();
	
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
			for (int i = 0; i < 60; i++) {
				String cmd = "sed -n '%s,%sp' %s";
				endLine = getEndLine(param.getLogFile());
				String readLogCmd = String.format(cmd, startLine, endLine, param.getLogFile());
				logger.info("readLogCmd: {}", readLogCmd);
				LinuxCommandUtil.exec(readLogCmd, logsQueueMap.get(queueKey));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startLine = endLine;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	int getEndLine(String logFile) throws Exception {
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
				logs.add(queue.poll());
			}
		}
		return logs;
	}

}
