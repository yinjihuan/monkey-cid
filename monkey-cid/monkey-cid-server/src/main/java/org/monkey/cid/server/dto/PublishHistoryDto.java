package org.monkey.cid.server.dto;


import java.util.List;

import lombok.Data;

@Data
public class PublishHistoryDto {
	
	private String publishId;
	
	private Long projectId;
	
	private String branch;
	
	private String publishMachines;
	
	private List<String> publishMachineList;
	
	private int publishMachineListCount;
	
	private int publishResult;
	
	private String errorMsg;
	
	private String jarPath;
	
	private String deployScript;
	
	private String publishTimeText;
	
	private String publishResultText;
	
	private String publishDescribe;
}
