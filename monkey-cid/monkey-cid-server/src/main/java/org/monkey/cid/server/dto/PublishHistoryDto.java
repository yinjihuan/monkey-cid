package org.monkey.cid.server.dto;


import lombok.Data;

@Data
public class PublishHistoryDto {
	private Long projectId;
	
	private String branch;
	
	private String publishMachines;
	
	private int publishResult;
	
	private String errorMsg;
	
	private String jarPath;
	
	private String deployScript;
}
