package org.monkey.cid.server.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectDto {

	private String id;
	
	private String name;
	
	private String chineseName;
	
	private String gitUrl;
	
	private String branchs;
	
	private String publishTime;
	
	private List<String> publishInstances;
	
	private String buildScript;
	
	private String dingToken;
	
}
