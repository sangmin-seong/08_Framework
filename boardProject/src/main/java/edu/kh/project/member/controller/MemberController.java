package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.dto.Member;
import edu.kh.project.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"loginMember"})
@Slf4j
@Controller
@RequestMapping("member")
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	
	/** 로그인
	 * @param memberEmail : 제출된 이메일
	 * @param memberPw : 제출된 비밀번호
	 * @param saveEmail : 이메일 저장 여부(체크안하면 null)
	 * @param ra : 리다이렉트 시 request scope로 값 전달하는 객체
	 * @param model : 데이터 전달용 객체(기본값 request scope)
	 * @param resp : 응답 방법을 제공하는 객체
	 * @return
	 */
	@PostMapping("login")
	public String login(
			@RequestParam("memberEmail") String memberEmail,
			@RequestParam("memberPw") String memberPw,			
			@RequestParam(name = "saveEmail", required = false) String saveEmail,			
			RedirectAttributes ra,
			Model model,
			HttpServletResponse resp
			) {
		
//		log.debug("memberEmail:{}",memberEmail);
//		log.debug("memberpw:{}",memberPw);
		
		Member loginMember = service.login(memberEmail, memberPw);
		
		
		if(loginMember == null) { // 로그인 실패
			ra.addFlashAttribute("message", "해당하는 정보가 없습니다.");
			
		}else { // 로그인 성공

					// loginMember를 session scope에 추가
					// 방법 1) HttpSession 이용
					// 방법 2) @SessionAttirbutes + Model 이용 방법
			
			/* Model을 이용해서 Session scope에 값 추가하는 방법 */
			// 1. model에 값 추가
			model.addAttribute("loginMember", loginMember);
			
			// 2. 클래스 선언부 위에 @SessionAttributes({"key"}) 추가
			//  -> key 값은 model에 추가된 key 값 "loginMember" 작성
			//   (request -> session)
			
			// @SessionAttributes :
			// Model에 추가된 값 중 session scope로 올리고 싶은 값의
			// key를 작성하는 어노테이션
			
			//----------------------------------------------------------------
			/* 이메일 저장 코드(Cookie) */
			
			// 1. Cookie 객체 생성
			Cookie cookie = new Cookie("saveEmail", memberEmail);
			
			// 2. 만들어진 Cookie가 사용된 경로(url)
			cookie.setPath("/"); // localhost 이하 모든 주소
			
			// 3. Cookie가 유지되는 시간(수명) 설정
			if(saveEmail == null) { // check X
				cookie.setMaxAge(0); // 만들어지자마자 만료
														 // == 기존에 만들어진 쿠키가 있으면 덮어씌우고 없어짐 
			}else { // check O
				cookie.setMaxAge(60 * 60 * 24 * 30); // 초단위로 작성
			}
		
			// 4. resp 객체에 추가해서 클라이언트에 전달
			resp.addCookie(cookie);
			
			
			//----------------------------------------------------------------
		}
		
		
		return "redirect:/"; // 메인페이지 리다이렉트
	}
	
	
	/** 로그아웃
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		/* SessionStatus
		 * - @SessionAttribute를 이용해 등록된
		 *   객체(값)의 상태를 관리하는 객체
		 * 
		 * - SessionStatus.setComplete();
		 * 	 -> 세션 상태 완료 == 없앰()
		 */
		
		status.setComplete();
		
		return "redirect:/"; // 메인페이지
	}
	
//---------------------------------------------
	/*
	 * 회원가입 페이지로 이동
	 */
	@GetMapping("signUp")
	public String signup() {
		return "member/signUp";
	}
	
	/** 회원가입 수행
	 * @param inputMember
	 * @param ra
	 * @return
	 */
	@PostMapping("signUp")
	public String signUp(
			@ModelAttribute Member inputMember,
			RedirectAttributes ra	) {
		
		int result = service.signUp(inputMember);
		
		String message = null;
		String path    = null;
		
		if(result > 0) {
			path = "/";
			message 
				= inputMember.getMemberNickname() + "님의 가입을 환영합니다😜";
		} else {
			path = "signUp";
			message = "회원 가입 실패...";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	/** 이메일 중복 검사(비동기)
	 * @param email : 입력된 이메일
	 * @return 0 : 중복X / 1: 중복
	 */
	@ResponseBody // 반환 값을 응답 본문(ajax 코드)로 반환
	@GetMapping("emailCheck")
	public int emailCheck(
			@RequestParam("email") String email
			) {
		return service.emailCheck(email);
	}
	
	@ResponseBody
	@GetMapping("nickCheck")
	public int nickCheck(
			@RequestParam("nickname") String nickname) {
		return service.nickCheck(nickname);
		
	}
	
	@ResponseBody
	@GetMapping("telCheck")
	public int telcheck(
			@RequestParam("tel") String tel) {
		return service.telCheck(tel);
	}
	
}



/* Cookie란?
 * - 클라이언트 측(브라우저)에서 관리하는 데이터(파일 형식)
 * 
 * - Cookie에는 만료기간, 데이터(key=value), 사용하는 사이트(주소)
 *  가 기록되어 있음
 *  
 * - 클라이언트가 쿠키에 기록된 사이트로 요청으로 보낼 때
 *   요청에 쿠키가 담겨져서 서버로 넘어감
 *   
 * - Cookie의 생성, 수정, 삭제는 Server가 관리
 *   저장은 Client가 함
 *   
 * - Cookie는 HttpServletResponse를 이용해서 생성,
 *   클라이언트에게 전달(응답) 할 수 있다
 */
