package edu.kh.project.member.service;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.project.member.dto.Member;
import edu.kh.project.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

//왜 Service 인터페이스 상속 받을까?
//- 팀 프로젝트, 유지 보수에 굉장히 도움이 많이되기 때문에!
//+ AOP Proxy 적용을 위해서

@Slf4j
@Service  // 비즈니스 로직을 처리하는 역할 명시 + Bean 등록(IOC)
public class MemberServiceImpl implements MemberService{
	
	@Autowired  // 등록된 Bean 중에서 같은 타입의 Bean을 대입(DI)
	private MemberMapper mapper;
	
	@Autowired // BCrypt 암호화 객체 의존성 주입 받기
	private BCryptPasswordEncoder encorder;
	
	/* 비밀번호 암호화
	 * - 하는 이유 : 평문 상태로 비밀번호 저장하면 안됨!!
	 * 
	 * - 아주 옛날 방식 : 데이터 -> 암호화 ,
	 * 										암호화된 데이터 -> 복호 -> 원본 데이터
	 * 
	 * - 약간 과거 / 현재 : 데이터를 암호화만 가능(SHA 방식)
	 * 											-> 복호화 방법 제공 X
	 *   -> 막 대입해서 만들경우 암호화 데이터 테이블에 뚫림
	 *   
	 * - 요즘 많이 사용하는 방식 : Bcrypt 암호화(Spring Security에 기본 내장)
	 * 
	 * - 입력된 문자열(비밀번호)에 salt를 추가한 후 암호화
        -> 암호화 할 때 마다 결과가 다름
        -> DB에 입력 받은 비밀번호를 암호화해서 넘겨줘도
        	 비교 불가능!!
        -> Bcrypt가 함께 제공하는 평문, 암호화 데이터 비교 메서드인
        	 matches()를 이용하면 된다!(같으면 true, 다르면 false)
        	 
        	--> matches() 메서드는 자바에서 동작하는 메서드
        	   -> DB에 저장된 암호화된 비밀번호를 조회해서 가져와야 한다! 
   *     	 
	 */
	
	// 로그인 서비스
	@Override
	public Member login(String memberEmail, String memberPw) {
		
		// 암호화 테스트
//		log.debug("memberPW : {}", memberPw);
//		log.debug("암호화된 memberPw : {}", encorder.encode(memberPw);
		
		// 1. memberEmail이 일치하는 정보를 DB에서 조회
		//    -> 비밀번호도 포함
		Member loginMember = mapper.login(memberEmail);
		
		
		// 2. 이메일(ID)가 일치하는 회원정보가 없을 경우
		if(loginMember == null) return null;
		
		
		// 3. DB에서 조회된 비밀번호와 입력받은 비밀번호가 같은지 확인
		//log.debug("비밀번호 일치? : {}", encorder.matches(memberPw, loginMember.getMemberPw()));
		
		// 입력받은 비밀번호와 DB에서 조회된 비밀번확 일치하지 않을 때
		if( !encorder.matches(memberPw, loginMember.getMemberPw()) ) {
			return null;
		}		
		
		// 4. 로그인 결과 반환
		return loginMember;
	}

	
	@Override
	public int signUp(Member inputMember) {
		
		// 1) 비밀번호 암호화
		String encPw = encorder.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		// 2) 주소 미입력 시 null로 변경
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
		}
		
		int result = mapper.signUp(inputMember);
		
		return result;
	}
	
	@Override
	public int emailCheck(String email) {
		return mapper.emailCheck(email);
	}
	
	
	@Override
	public int nickCheck(String nickname) {
		return mapper.nickCheck(nickname);
	}
	
	@Override
	public int telCheck(String tel) {
		return mapper.telCheck(tel);
	}
	
	
	
	
	
}
