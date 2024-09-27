package edu.kh.project.fileUpload.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;

@Mapper
public interface FileUploadMapper {

	
	/** 파일 1개 정보 DB에 삽입
	 * @return
	 */
	int fileInsert(FileDto file);
	
	/** 파일 목록 조회
	 * @return
	 */
	List<FileDto> selectFileList();

}
