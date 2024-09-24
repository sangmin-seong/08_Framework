package edu.kh.semiProject.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.semiProject.member.dto.Member;

@Mapper
public interface MemberMapper {

	/** memberEmail이 일치하는 회원 정보 조회
	 * @param memberEmail
	 * @return loginMember / null
	 */
	Member login(String memberEmail);
	
}
