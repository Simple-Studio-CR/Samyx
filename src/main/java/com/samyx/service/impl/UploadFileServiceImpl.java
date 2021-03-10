package com.samyx.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.samyx.service.interfaces.IUploadFileService;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String UPLOADS_FOLDER = "/opt/uploads/biopasos";

	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		this.log.info("pathFoto: " + pathFoto);
		UrlResource urlResource = new UrlResource(pathFoto.toUri());
		if (!urlResource.exists() || !urlResource.isReadable())
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		return (Resource) urlResource;
	}

	public String copy(MultipartFile file) throws IOException {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFilename);
		this.log.info("rootPath: " + rootPath);
		Files.copy(file.getInputStream(), rootPath, new java.nio.file.CopyOption[0]);
		return uniqueFilename;
	}

	public boolean delete(String filename) {
		if (filename != null && filename.length() > 0) {
			Path rootPath = getPath(filename);
			File archivo = rootPath.toFile();
			if (archivo.exists() && archivo.canRead() && archivo.delete())
				return true;
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get("/opt/uploads/biopasos", new String[0]).resolve(filename).toAbsolutePath();
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get("/opt/uploads/biopasos", new String[0]).toFile());
	}

	public void init() throws IOException {
		Files.createDirectory(Paths.get("/opt/uploads/biopasos", new String[0]),
				(FileAttribute<?>[]) new FileAttribute[0]);
	}
}
