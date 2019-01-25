package org.monkey.cid.server.param;

import java.util.List;

import lombok.Data;

@Data
public class PublishProjectParam {
	private String projectId;
	private String buildScript;
	private String publishProjectName;
	private String publishBranch;
	private String gitUrl;
	private List<String> publishMachineList;
	private String jarPath;
}
