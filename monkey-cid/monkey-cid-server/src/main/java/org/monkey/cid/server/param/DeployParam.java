package org.monkey.cid.server.param;

import lombok.Data;

@Data
public class DeployParam {

	private String deployScript;
	
	private String logFile;
	
	private String projectName;
	
	private String branch;
	
}
