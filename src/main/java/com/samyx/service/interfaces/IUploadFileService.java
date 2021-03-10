package com.samyx.service.interfaces;

import java.io.IOException;
import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	Resource load(String paramString) throws MalformedURLException;

	String copy(MultipartFile paramMultipartFile) throws IOException;

	boolean delete(String paramString);

	void deleteAll();

	void init() throws IOException;
}
