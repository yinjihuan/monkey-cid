package org.monkey.cid.client.service.impl;

import org.monkey.cid.client.param.DeployParam;
import org.monkey.cid.client.service.NoticeService;
import org.monkey.cid.core.util.LinuxCommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {

	private Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);
	
	@Override
	public boolean deployProject(DeployParam param) {
		//String deployScriptPath = "/home/goojia/deploy_fangjia_youfang_service.sh";
		logger.info("deployProject: {}", param.getDeployScript());
		LinuxCommandUtil.exec(param.getDeployScript(), null);
		return true;
	}

}
