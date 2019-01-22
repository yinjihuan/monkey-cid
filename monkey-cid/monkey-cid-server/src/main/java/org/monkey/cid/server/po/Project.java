package org.monkey.cid.server.po;

import java.util.Date;

import com.cxytiandi.jdbc.annotation.AutoId;
import com.cxytiandi.jdbc.annotation.Field;
import com.cxytiandi.jdbc.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "project", author = "yinjihuan", desc = "项目表")
public class Project {

	@AutoId
	@Field(value = "id", desc = "ID")
	private Long id;
	
	@Field(value = "name", desc = "项目名")
	private String name;
	
	@Field(value = "chinese_name", desc = "项目中文名")
	private String chineseName;
	
	@Field(value = "git_url", desc = "Git地址")
	private String gitUrl;
	
	@Field(value = "add_time", desc = "添加时间")
	private Date addTime;
	
	@Field(value = "update_time", desc = "修改时间")
	private Date updateTime;

}
