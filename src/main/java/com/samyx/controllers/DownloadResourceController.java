package com.samyx.controllers;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DownloadResourceController {
	@Value("${path.upload.files.api}")
	private String pathUploadFilesApi;

	@GetMapping({ "/logo/{filename:.+}" })
	public ResponseEntity<Resource> getLogo(@PathVariable String filename) {
		Resource recurso = null;
		String ruta = "logo";
		try {
			recurso = renderFile(filename, ruta);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().header("Content-Disposition",
				new String[] { "attachment; filename=\"" + recurso.getFilename() + "\"" })).body(recurso);
	}

	@GetMapping({ "/mr-inbox/{filename:.+}" })
	public ResponseEntity<Resource> getFilesInbox(@PathVariable String filename) {
		Resource recurso = null;
		String ruta = "mr-automatico";
		try {
			recurso = renderFile(filename, ruta);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ((ResponseEntity.BodyBuilder) ResponseEntity.ok()

				.header("Content-Disposition",
						new String[] { "attachment; filename=\"" + recurso.getFilename() + "\"" })).body(recurso);
	}

	@GetMapping({ "/llave/{emisor}/{filename:.+}" })
	public ResponseEntity<Resource> getLlave(@PathVariable String emisor, @PathVariable String filename) {
		Resource recurso = null;
		String ruta = emisor + "/cert";
		try {
			recurso = renderFile(filename, ruta);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().header("Content-Disposition",
				new String[] { "attachment; filename=\"" + recurso.getFilename() + "\"" })).body(recurso);
	}

	public Resource renderFile(String filename, String ruta) throws MalformedURLException {
		UrlResource urlResource = new UrlResource(Paths.get(this.pathUploadFilesApi + "/" + ruta, new String[0])
				.resolve(filename).toAbsolutePath().toUri());
		if (!urlResource.exists() || !urlResource.isReadable())
			throw new RuntimeException("Error: no se puede cargar la imagen: " + filename.toString());
		return (Resource) urlResource;
	}
}
