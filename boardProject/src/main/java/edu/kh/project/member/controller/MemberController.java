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
		
		
		Member loginMember = service.login(memberEmail, memberPw);
		
		
		if(loginMember == null) { // 로그인 실패
			ra.addFlashAttribute("message", "해당하는 정보가 없습니다.");
			
		}else { // 로그인 성공

			
			model.addAttribute("loginMember", loginMember);
			
			
			//----------------------------------------------------------------
			/* 이메일 저장 코드(Cookie) */
			
			// 1. Cookie 객체 생성
			Cookie cookie = new Cookie("saveEmail", memberEmail);
			
			cookie.setPath("/"); 
			
			if(saveEmail == null) { 
				cookie.setMaxAge(0); 
														  
			}else { 
				cookie.setMaxAge(60 * 60 * 24 * 30); 
			}
		
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
		
		status.setComplete();
		
		return "redirect:/"; // 메인페이지
	}
	
//---------------------------------------------
	/*
	 * 회원가입 페이지로 이동
	 */
	@GetMapping("signUp")
	public String signUp() {
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



