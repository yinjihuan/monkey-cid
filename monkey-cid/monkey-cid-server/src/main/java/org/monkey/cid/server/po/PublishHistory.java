package org.monkey.cid.server.po;

import java.util.Date;

import com.cxytiandi.jdbc.annotation.AutoId;
import com.cxytiandi.jdbc.annotation.Field;
import com.cxytiandi.jdbc.annotation.TableName;

import lombok.Data;

@Data
@TableName(value = "publish_history", author = "yinjihuan", desc = "发布历史表")
public class PublishHistory {

	@AutoId
	@Field(value = "id", desc = "ID")
	private Long id;
	
	@Field(value = "project_id", desc = "项目ID")
	private Long projectId;
	
	@Field(value = "branch", desc = "发布分支")
	private String branch;
	
	@Field(value = "publish_machines", desc = "发布机器")
	private String publishMachines;
	
	@Field(value = "publish_time", desc = "发布时间")
	private Date publishTime;
	
	@Field(value = "publish_result", desc = "发布结果(0成功 1失败)")
	private int publishResult;
	
	@Field(value = "error_msg", desc = "异常信息")
	private String errorMsg;
	
	@Field(value = "jar_path", desc = "编译后的程序地址")
	private String jarPath;
	
	@Field(value = "deploy_script", desc = "启动脚本")
	private String deployScript;
	
	@Field(value = "publish_describe", desc = "发布描述")
	private String publishDescribe;
	
}
