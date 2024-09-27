package edu.kh.project.fileUpload.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;

public interface FileUploadService {
	
	/** 단일 파일 업로드
	 * @param uploadFile
	 * @return
	 */
	String test1(MultipartFile uploadFile) throws IllegalStateException, IOException;

	
	/** 업로드된 파일 리스트 조회
	 * @return
	 */
	List<FileDto> selectFileList();

	/** 파일 업로드 시 지정된 이름으로 변경하여 업로드
	 * @param uploadFile
	 * @param fileName
	 * @return
	 */
	String test2 (MultipartFile uploadFile, String fileName) throws IllegalStateException, IOException;

	
	/**
	 * @param uploadFile
	 * @return
	 */
	String test3(MultipartFile uploadFile);

}
