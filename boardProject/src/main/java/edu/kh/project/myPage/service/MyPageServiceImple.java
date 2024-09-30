package edu.kh.project.myPage.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.mapper.MyPageMapper;

@PropertySource("classpath:/config.properties")
@Transactional // 서비스 내 메서드 수행 중 
							// UnCheckedException 발생 시 rollback 수행
							// 아니면 메서드 종료 시 commit 수행
@Service
public class MyPageServiceImple implements MyPageService{

	@Autowired // 등록된 Bean 중에서 같은 자료형의 Bean을 의존성 주입(DI)
	private MyPageMapper mapper;
	
	@Autowired // 의존성 주입(DI)
	private BCryptPasswordEncoder encoder;
	
	@Value("${my.profile.web-path}")
	private String profileWebPath; 
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath;
	
	
	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember) {
		
		// 만약 주소가 입력되지 않은 경우 null로 변경
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
			// UPDATE 구문 수행 시 MEMBER_ADDRESS 컬럼 값이 NULL이 됨
		}
		
		
		return mapper.updateInfo(inputMember);
	}
	
	// 닉네임 중복 검사
	@Override
	public int checkNickname(String input) {
		return mapper.checkNickname(input);
	}
	
	// 비밀번호 변경
	@Override
	public int changePw(String currentPw, String newPw,Member loginMember) {
		
		// 1) 입력 받은 현재 비밀번호가
		//		로그인한 회원의 비밀번호와 일치하는지 검사
		//		(BCryptPasswordEncoder.matches(평문, 암호문) 이용)
		
		// 비밀번호가 일치하지 않으면
		if( encoder.matches(currentPw, loginMember.getMemberPw()) == false) {
			return 0;
		}
		
		
		// 2) 새 비밀번호 암호화
		String encPw = encoder.encode(newPw);
		
		// 3) DB 비밀번호 변경(회원번호, 암호화된 새 비밀번호)
		loginMember.setMemberPw(encPw); // 세션에 저장된 회원 정보 중 Pw를 변경
		return mapper.changePw( loginMember.getMemberNo(), encPw);
	}
	
	
	@Override
	public int secession(String memberPw, Member loginMember) {

	// 1) 입력 받은 현재 비밀번호가
			//		로그인한 회원의 비밀번호와 일치하는지 검사
			//		(BCryptPasswordEncoder.matches(평문, 암호문) 이용)
			
			// 비밀번호가 일치하지 않으면
			if( encoder.matches(memberPw, loginMember.getMemberPw()) == false) {
				return 0;
			}
						
			// 2) DB 회원정보 변경(회원번호 일치여부 확인)
			return mapper.secession( loginMember.getMemberNo());
	}
	
	// 회원프로필 이미지 수정
	@Override
	public String profile(MultipartFile profileImg, int memberNo) {
		
		// 1) 파일 업로드 확인
		if(profileImg.isEmpty()) {
			
			// 제출된 파일이 없음 == X 버튼을 눌러 기본 이미지로 변경
			// == DB에 저장된 이미지 경로가 NULL
			
			int result = mapper.profile(null, memberNo);
			
			
			
			return null;
		}
			
		
		// 2) 파일명 변경
		String rename = FileUtil.rename(profileImg.getOriginalFilename());
		
		// 3) 웹 접근 경로(config.properties) + 변경된 파일명
		String url = profileWebPath + rename; 
		
		// 4) DB에 INSERT 
		int result = mapper.profile(url, memberNo);
		
		if(result == 0) return null;
		
		try {
			
			// c:/uploadFiles/profile/ 폴더가 없으면 생성
			File folder = new File(profileFolderPath);
			if(!folder.exists()) folder.mkdirs();
			
			// 업로드되어 임시저장된 이미지를 지정된 경로에 옮기기
			profileImg.transferTo(
						new File(profileFolderPath + rename));
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
			throw new FileUploadFailException("프로필 이미지 수정 실패");
		}
		
		// 업로드된 파일로 접근할 수 있는 웹경로 반환
		return profileWebPath + rename;
	}
	
}
