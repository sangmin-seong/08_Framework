package edu.kh.project.fileUpload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import edu.kh.project.fileUpload.dto.FileDto;
import edu.kh.project.fileUpload.mapper.FileUploadMapper;
import lombok.RequiredArgsConstructor;

//@Transactional
//- Unchecked Excpetion 발생 시 Rollback 수행

//@Transactional(rollbackFor = Exception.class)
//- Exception 이하 예외 발생 시 Rollback 수행
//== Checked, Unchecked 가리지 않고 예외 발생 시 롤백

@Service
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
@Transactional(rollbackFor = Exception.class)
public class FileUploadServiceImple implements FileUploadService {
	
	private final FileUploadMapper mapper;
	
	// 인터넷 요청 주소
	@Value("${my.test.web-path}")
	private String testWebPath; // 
	
	// 파일 저장 경로
	@Value("${my.test.folder-path}")
	private String testFolderPath;

	
	// 단일 파일 업로드
	@Override
	public String test1(MultipartFile uploadFile) throws IllegalStateException, IOException {

			// MultipartFile이 제공하는 메서드
		
			// - getSize() : 파일 크기
			// - isEmpty() : 업로드한 파일이 없을 경우 true
			// - getOriginalFileName() : 원본 파일 명
			// - transferTo(경로) : 
			//    메모리 또는 임시 저장 경로에 업로드된 파일을
			//    원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
		
		//1) 업로드 된 파일이 있는지 검사
		if(uploadFile.isEmpty()) { // 업로드 X
			return null;
		}
		
		// 2) 지정된 경로에 파일 저장
		File folder = new File(testFolderPath);
		
		// C:/uploadFiles/ 폴더가 없을 경우
		if(folder.exists() == false) {
			folder.mkdirs(); // 폴더 생성
		}
		
		/* DB에 업로드되는 파일정보를 insert 
		 * -> DB INSERT 후 파일 저장 순서로 동작
		 * 
		 * 		(만약에) 파일 저장 중 예외가 발생
		 * 		-> @Transactinal 어노테이션이 Rollback 수행
		 *    -> INSERT 취소
		 * */
		
		/* 원본파일명을 중복되지 않는 이름으로 변경 */
		String rename = FileUtil.rename(uploadFile.getOriginalFilename());
		
		// FileDto 객체를 만들어 INSERT에 필요한 정보를 Set
		FileDto file = FileDto.builder()
										.fileOriginalName(uploadFile.getOriginalFilename())
										.fileRename(rename)
										.filePath(testWebPath)
										.build();
	
		int result = mapper.fileInsert(file);
		
		
		// 업로드 되어 메모리 또는 임시 저장 폴더에 저장된 파일을 
		// 지정된 경로(path)로 전달하는(옮기는) 코드
		uploadFile.transferTo(
				new File(testFolderPath + rename));	
		
		
		
		
		// 웹에서 접근 가능한 파일 경로(URL) 반환
		return testWebPath + rename;
	}
	
	
	/**
	 * 업로드 된 파일 목록 조회
	 */
	@Override
	public List<FileDto> selectFileList() {
		
		return mapper.selectFileList();
	}
	
	/**
	 * 파일 업로드 시 지정된 이름으로 변경하여 업로드	
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public String test2(MultipartFile uploadFile, String fileName) throws IllegalStateException, IOException {
		
		// 1) 업로드 된 파일 존재유무 검사
		// if(uploadFile.size() <= 0)
		// if(uploadFile.getOriginalFileName.equals("")
		if(uploadFile.isEmpty()) {
			return null;
		}

		// 2) 제출된 fileName이 없다면 기존 파일명 유지
		
		// 확장자 추출
		int index = uploadFile.getOriginalFilename().lastIndexOf(".");
		String ext = uploadFile.getOriginalFilename().substring(index);
		
		
		String originalName = 
				fileName.equals("") ? uploadFile.getOriginalFilename()
														: fileName + ext; // 입력된 파일명.확장자
		
		// 3) 파일명 변경하기
		String rename = FileUtil.rename(originalName);
		
		
		// 4) DB에 파일 정보부터 INSERT
		FileDto file = FileDto.builder()
									.fileOriginalName(originalName)
									.fileRename(rename)
									.filePath(testWebPath)
									.build();
		
		
		int result = mapper.fileInsert(file);
		
		// 5) 지정된 폴더로 임시저장된 업로드 파일을 전송하기
		uploadFile.transferTo(
				new File(testFolderPath + rename));
		 
		 return testWebPath + rename;
	}
	
	// 단일 파일 업로드 + 사용자 정의 예외를 이용한 예외 처리

	@Override
	public String test3(MultipartFile uploadFile) {
		
		// 1) 업로드된 파일이 있는지 검사
		if(uploadFile.isEmpty()) return null;
		
		// 2) 파일명 변경
		String rename = FileUtil.rename(uploadFile.getOriginalFilename());
		
		// 3) DB에 파일정보 INSERT
		FileDto file = FileDto.builder()
									.fileOriginalName(uploadFile.getOriginalFilename())
									.fileRename(rename)
									.filePath(testWebPath)
									.build();
									
		int result = mapper.fileInsert(file);
		
		try {
			uploadFile.transferTo(
					new File(testFolderPath + rename)
					);
		} catch (Exception e) {
			e.printStackTrace();
			
			// transferTo()는 Checked Exception을 던지기 때문에

			// 1) throws 또는 try/catch를 무조건 작성해야한다
			
			// 2) throws 작성 시 호출하는 메서드에서
			//  	추가 예외처리 코드를 작성해야되는 번거로움이 있음
			
			// 3) try/catch 작성 시
			//  	메서드 내부에서 예외가 처리되어
			//		메서드 종료 시 예외가 던져지지 않아
			//		@Transactional이 rollback을 수행할 수 없게 된다
			
			// [추천되는 해결방법]

			// * try/catch를 작성해서 Checked Exception을 처리
			// 	 - 호출하는 메서드에 throws 구문 작성X

			//	* Unchecked Exception 형태의 사용자 정의 예외 강제 발생
			//	 - @Transactional 어노테이션에
			//	 	 rollbackFor 속성 작성 안해도 롤백 처리 가능
			
			// 예외 강제 발생
			// - Unchecked Exception은 
			//   컴파일러가 자동으로 throws 구문을 작성해줘서
			//	 예외 발생시 호출부로 던져지게 됨
			throw new FileUploadFailException();
		}
		
		return testWebPath + rename;
	}

}
