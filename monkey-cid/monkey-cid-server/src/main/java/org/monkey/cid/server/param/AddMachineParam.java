package org.monkey.cid.server.param;

import lombok.Data;

@Data
public class AddMachineParam {

	private String id;
	
	private String intranetIp; 
	
	private String extranetIp;
	
	private String clientUrl;
	
	private String publishCatalog;
	
	private String environment;
	
}
