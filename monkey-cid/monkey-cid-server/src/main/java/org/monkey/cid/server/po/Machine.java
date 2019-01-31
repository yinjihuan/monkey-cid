package org.monkey.cid.server.po;

import java.util.Date;

import com.cxytiandi.jdbc.annotation.AutoId;
import com.cxytiandi.jdbc.annotation.Field;
import com.cxytiandi.jdbc.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "machine", author = "yinjihuan", desc = "机器表")
public class Machine {

	@AutoId
	@Field(value = "id", desc = "ID")
	private Long id;
	
	@Field(value = "intranet_ip", desc = "内网IP")
	private String intranetIp; 
	
	@Field(value = "extranet_ip", desc = "外网IP")
	private String extranetIp;
	
	@Field(value = "client_url", desc = "CID Client地址")
	private String clientUrl;
	
	@Field(value = "publish_catalog", desc = "发布目录")
	private String publishCatalog;
	
	@Field(value = "environment", desc = "所属环境")
	private String environment;
	
	@Field(value = "add_time", desc = "添加时间")
	private Date addTime;
	
	@Field(value = "update_time", desc = "修改时间")
	private Date updateTime;
	
}
