package edu.kh.semiProject.member.service;

import edu.kh.semiProject.member.dto.Member;

public interface MemberService {

	/** 로그인 서비스
	 * @param memberEmail
	 * @param memberPw
	 * @return loginMember 또는 null(email 또는 pw 불일치)
	 */
	Member login(String memberEmail, String memberPw);

}
