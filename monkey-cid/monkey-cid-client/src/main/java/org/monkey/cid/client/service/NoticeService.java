package org.monkey.cid.client.service;

import java.util.List;

import org.monkey.cid.client.param.DeployParam;

public interface NoticeService {

	boolean deployProject(DeployParam param);
	
	List<String> queryDeployLogs(String projectName, String branch);
}
