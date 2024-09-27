package edu.kh.project.fileUpload.controller;

import java.io.IOException;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.eclipse.angus.mail.handlers.multipart_mixed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;
import edu.kh.project.fileUpload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;

@Slf4j
@Controller
@RequestMapping("fileUpload")
@RequiredArgsConstructor
public class FileUplodaController {
	
	private final FileUploadService service;
	
	
	@GetMapping("main")
	public String fileUploadMain(
			Model model) {
		List<FileDto> fileList = service.selectFileList();
		
		model.addAttribute("fileList", fileList);
		
		return "fileUpload/main";
	}
	
	/* Spring에서 제출된 파일을 처리하는 방법
	 * 
	 * 1) enctype = "multipart/form-data"로 
	 * 		클라이언트가 요청
	 * 
	 * 2) Spring에 내장되어 있는
	 * 		MultiPartResolver가 제출된 파라미터를 분류함
	 * 		
	 * 		문자열, 숫자 -> String
	 * 		파일         -> MultiPartFile 인터페이스 구현한 객체
	 * 
	 * 3) 컨트롤러 메서드 매개 변수로 전달
	 * 		(@RequestParam, @ModelAttribute)
	 */
	
	/** 단일 파일 업로드
	 * @param uploadFile
	 * @return
	 */
	@PostMapping("test1")
	public String test1(
			@RequestParam("uploadFile") MultipartFile uploadFile) throws IllegalStateException, IOException  {

		String filePath = service.test1(uploadFile);
		
		log.debug("업로드 된 파일 경로 : {}", filePath);
		
		return "redirect:main";
	}
	
	/** 파일 업로드 시 지정된 이름으로 변경하여 업로드
	 * @param uploadFile
	 * @param fileName
	 * @return
	 */
	@PostMapping("test2")
	public String test2(
			@RequestParam("uploadFile") MultipartFile uploadFile,
			@RequestParam("fileName") String fileName
			)throws IllegalStateException, IOException {
		
		String filePath = service.test2(uploadFile, fileName);
		
		log.debug("업로드 된 파일 경로 : {}", filePath);
		
		return "redirect:main";
	}
	
	
	@PostMapping("test3")
	public String test3(
			@RequestParam("uploadFile") MultipartFile uploadFile
			) {
		
		String filePath = service.test3(uploadFile);
		
		log.debug("업로드 된 파일 경로 : {}", filePath);
		
		return "redirect:main";
	}
}
