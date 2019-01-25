package org.monkey.cid.client.controller;

import java.io.File;
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
	
	@PostMapping("/file/upload")
	public ResponseData<Void> fileUpload(@RequestParam(value = "file") MultipartFile file, String fileFolder, String fileName) {
		logger.info(fileFolder);
		logger.info(fileName);
		if (!StringUtils.hasText(fileFolder)) {
			return Response.fail("fileFolder不能为空");
		}
		if (!StringUtils.hasText(fileName)) {
			return Response.fail("fileName不能为空");
		}
		try {
			byte[] bytes = file.getBytes();
			File folder = new File(fileFolder);
		    if (!folder.exists() && !folder.isDirectory()) {  
		    	folder.mkdirs();  
		    }  
			FileCopyUtils.copy(bytes, new File(fileFolder + File.separator + fileName));
		} catch (Exception e) {
			logger.error("代码包上传异常", e);
			return Response.fail("代码包上传异常：" + e.getMessage());
		}
		return Response.ok();
	}
	
}
