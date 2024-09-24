package edu.kh.project.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.dto.Member;

@Mapper
public interface MemberMapper {

	/** memberEmail이 일치하는 회원 정보 조회
	 * @param memberEmail
	 * @return loginMember / null
	 */
	Member login(String memberEmail);

	int signUp(Member inputMember);

	int emailCheck(String email);

	int nickCheck(String nickname);

	int telCheck(String tel);
	
	
	
}
