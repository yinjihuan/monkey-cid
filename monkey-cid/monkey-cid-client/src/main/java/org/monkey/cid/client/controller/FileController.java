package org.monkey.cid.client.controller;

import java.io.File;
import java.io.IOException;

import org.monkey.cid.core.base.Response;
import org.monkey.cid.core.base.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

	private Logger logger = LoggerFactory.getLogger(FileController.class);
	
	/**
	 * 文件上传，上传编译好的代码，存放在服务器指定的位置
	 * @param file
	 * @param fileFullName 文件全称，包含磁盘路径
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/file/upload")
	public ResponseData<Void> fileUpload(@RequestParam(value = "file") MultipartFile file, String fileFullName) {
		if (!StringUtils.hasText(fileFullName)) {
			return Response.fail("fileFullName不能为空");
		}
		try {
			byte[] bytes = file.getBytes();
			File fileToSave = new File(fileFullName);
			FileCopyUtils.copy(bytes, fileToSave);
		} catch (Exception e) {
			logger.error("代码包上传异常", e);
			return Response.fail("代码包上传异常：" + e.getMessage());
		}
		return Response.ok();
	}
	
}
