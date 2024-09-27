package edu.kh.project.myPage.service;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.dto.Member;

public interface MyPageService {

	/** 회원정보 수정
	 * @param member
	 * @return
	 */
	int updateInfo(Member inputMember);

	/** 닉네임 중복 검사
	 * @param input
	 * @return
	 */
	int checkNickname(String input);

	int changePw(String currentPw, String newPw, Member loginMember);

	int secession(String memberPw, Member loginMember);

	/**로그인한 회원의 프로필 이미지 수정
	 * @param profileImg
	 * @param memberNo
	 * @return filePath
	 */
	String profile(MultipartFile profileImg, int memberNo);

}
