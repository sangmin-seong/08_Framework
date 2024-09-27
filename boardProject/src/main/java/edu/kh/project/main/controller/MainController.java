package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.main.service.MainService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

@SessionAttributes("loginMember")
@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final MainService service;

	@RequestMapping("/")
	public String mainPage() {
		return "common/main";
	}
	
	/** 비동기 회원목록 조회
	 * @return
	 */
	@ResponseBody
	@GetMapping("selectMemberList")
	public List<Member> selectMemberList(){
		return service.selectMemberList();
	}
	
	
	/** 빠른 로그인
	 * @param memberNo
	 * @param model
	 * @return
	 */
	@PostMapping("directLogin")
	public String directLogin(
			@RequestParam("memberNo") int memberNo,
			Model model
			) {
		
		Member loginMember = service.directLogin(memberNo);
		
		// 로그인된 회원 정보를 session에 추가
		model.addAttribute("loginMember", loginMember); // request Scope 기본값
		
		
		
		
		
		return "redirect:/";
	}
	
	
	/** 비밀번호 초기화
	 * @param memberNo
	 * @return
	 */
	@ResponseBody
	@PostMapping("resetPw")
	public int resetPw(
			@RequestBody int memberNo
			) {
		return service.resetPw(memberNo); 
	}
	
	
	@ResponseBody
	@PutMapping("changeStatus")
	public int changeStatus(
			@RequestBody int memberNo) {
		return service.changeStatus(memberNo);
	}
}
