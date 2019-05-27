package at.ac.univie.rscm.spring.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.application.global.data.DownloadFileInfo;
import at.ac.univie.rscm.application.global.data.UploadFileResponse;
import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.Role;
import at.ac.univie.rscm.model.Scriptexecution;
import at.ac.univie.rscm.spring.api.FileStorageService;
import at.ac.univie.rscm.spring.api.repository.ScriptexecutionRepository;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

//https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
@RestController
@RequestMapping("/FileManager/")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	private GlobalSettingsAndVariablesInterface gsav;

	public FileController() {
		gsav = GlobalSettingsAndVariables.getInstance();
	}

	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private ScriptexecutionRepository scriptexecutionRepository;

	@PostMapping("/deleteFile")
	public String deleteFile(@RequestParam String[] fileName) {
		File f = new File(gsav.getFileDownloadDirectory()+"\\" + fileName[0]);

		List<Scriptexecution> seList = scriptexecutionRepository.findByScriptNameAndExDateNotNull(fileName[0]);
		if(seList.size()==0 ) {
			scriptexecutionRepository.deleteAll(seList);
			if(f.delete()) {
				return "The File with name: " + fileName[0] + " was deleted successfully" ;
			}else {
				return "fileNot found";
			}
		}else {
			return "There are other objects that depend on the file. <br>"
					+ "There are "+seList.size() +" ScriptExecutions that depend on this file <br>"
					+ "If you really want to delete this file press here:<br>"
					+ "<button class=\"w3-button w3-red\" onclick=\"forceDeleteFile('"+fileName[0]+"')\">force delete</button>";
		}		
	}
	
	@PostMapping("/forceDeleteFile")
	public String forceDeleteFile(@RequestParam String[] fileName) {
		File f = new File(gsav.getFileDownloadDirectory()+"\\" + fileName[0]);
		List<Scriptexecution> seList = scriptexecutionRepository.findByScriptNameAndExDateNotNull(fileName[0]);
		scriptexecutionRepository.deleteAll(seList);
		if(f.delete()) {
			return "The File with name: " + fileName[0] + " was deleted successfully" ;
		}else {
			return "fileNot found";
		}
		
	}
	
	
	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if(fileExists(fileName)) {
			return new UploadFileResponse(fileName, "", "", -1);
		}else {
			fileName = fileStorageService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(fileName).toUriString();

			return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		}
		

	}

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	@PostMapping("/getFileList")
	public List<DownloadFileInfo> getFileList(@RequestParam("search") String[] search) {
		List<DownloadFileInfo> list = new ArrayList<DownloadFileInfo>();
		String s = "";
		if (search.length != 0) {
			s = search[0];
		}

		File f = new File(gsav.getFileDownloadDirectory()); // current directory

		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				if (s.equals("") || file.getName().contains(s)) {
					try {
						// https://www.programcreek.com/java-api-examples/?class=java.nio.file.Files&method=readAttributes
						Path filePath = file.toPath();
						BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
						list.add(new DownloadFileInfo(file.getName(), attr.creationTime().toString(),
								file.getCanonicalPath(), attr.size()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}

		return list;
	}
	
	private boolean fileExists(String fileName) {
		File f = new File(gsav.getFileDownloadDirectory()); // current directory

		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				if(file.getName().equals(fileName)) {
					return true;
				}
			}

		}
		return false;
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}