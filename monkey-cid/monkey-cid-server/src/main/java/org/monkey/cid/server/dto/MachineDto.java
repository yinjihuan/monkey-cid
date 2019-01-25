package org.monkey.cid.server.dto;

import lombok.Data;

@Data
public class MachineDto {

	private String id;
	
	private String intranetIp;
	
	private String extranetIp;
	
	private String clientUrl;
	
	private String publishCatalog;
	
}
