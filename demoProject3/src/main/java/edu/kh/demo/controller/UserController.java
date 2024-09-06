package edu.kh.demo.controller;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.demo.dto.UserDto;
import edu.kh.demo.service.UserService;
import edu.kh.demo.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService service;
	
	
	
	/** 사용자 번호를 입력받아 일치하는 사용자의 이름 조회
	 * @param userNo : 제출된 파라미터 중 key 값이 "userNo"인 값
	 * @param model  : Sptring에서 사용하는 데이터 전달용 객체(request)
	 * @return
	 */
	@GetMapping("test1")
	public String selectUserName(
			@RequestParam("userNo") int userNo,
			Model model
			) {
	
		// 사용자 이름 조회 서비스 호출 후 결과 반환받기
		String userName = service.selectUserName(userNo);
		
		// 조회결과 model에 추가
		model.addAttribute("userName", userName);		
		
		// templates 폴더로 forward
		// classpath:/templates/user/searchName.html 요청 위임
		return "user/searchName";
	}
	
	
	@GetMapping("selectAll")
	public String selectAll(Model model) {
		
		List<UserDto> userList = service.selectAll();
		
		model.addAttribute("userList", userList);
		
		return "user/selectAll";
	}
	
	
	/* RedirectAttributes
	 * - 리다이렉트 시 request scope로 값을 전달할 수 있는
	 * 	 스프링 객체
	 * 
	 * 	[원리]
	 * 	
	 * 	1) 값 세팅(request scope)
	 * 
	 * 	2) 리다이렉트 수행되려고 할 때,
	 * 		 1)에서 세팅된 값을 session scope로 대피
	 * 	
	 * 	3) 리다이렉트 수행 후, 
	 * 		 session에 대피했던 값을 다시 request scope로 가져옴
	 */
	/** userNo가 일치하는 사용자 조회
	 * @param userNo : 주소에 작성된 사용자 번호
	 * @param model : 데이터 전달용 객체
	 * @param ra : 
	 * @return
	 */
	@GetMapping("select/{userNo}")
	public String selectUser(
		@PathVariable("userNo") int userNo,
		Model model,
		RedirectAttributes ra) {
		
		UserDto user = service.selectUser(userNo);
		
		
		if(user != null){ // 조회 결과가 있을 경우
			model.addAttribute("user", user);
			return "user/selectUser";
		}
		
		// 조회 결과가 없을 경우
		
		// 리다이렉트 시 잠깐 session으로 대피할 값 추가
		ra.addFlashAttribute("message", "해당 user 번호를 가진 사용자가 없습니다.");
		
		// 목록으로 redirect
		return "redirect:/user/selectAll";
		
	}
	
	/* @ModelAttribute 
	 *  - 전달된 파라미터의 key(name 속성) 값이
	 *    작성된 DTO의 필드명과 일치하면
	 *    DTO 객체의 필드에 자동으로 세팅하는 어노테이션
	 *    --> 이렇게 만들어진 객체를 "커맨드 객체" 라고함
	 */
	
	
	/** 사용자 정보 수정
	 * @param userNo : 주소에 포함된 userNo
	 * @param user : userNo, userPw, userName이 포함된 커맨드 객체
	 * @param ra : 리다이렉트 시, request scope로 값 전달 객체
	 * @return
	 */
	@PostMapping("update/{userNo}")
	public String updateUser(
			@PathVariable("userNo") int userNo,
			@ModelAttribute UserDto user,
			RedirectAttributes ra
		) {
		log.debug("userNo : {}", userNo);
		log.debug("user   : {}", user);
		// UserDto user 
		//  == 제출된 userPw, userName + @PathVariable("userNo")
		
		int result = service.updateUser(user);
		
		// 수정 결과에 따른 메시지 지정
		String message = null;
		
		if(result > 0) 	message = "수정 성공!";
		else            message = "수정 실패!";
		
		// ra에 값 추가
		ra.addFlashAttribute("message", message);
		
		
		// 상세 조회 페이지로 redirect
		return "redirect:/user/select/"+ userNo;
	}	
	
	@PostMapping("delete/{userNo}")
	public String deleteUser(
			@PathVariable("userNo") int userNo,
			RedirectAttributes ra
			) {
		int result = service.deleteUser(userNo);
		
		// 삭제 여부에 따라 redirect 경로, 메시지 지정하기
		String path = null;
		String message = null;
		
		if(result > 0) {
			path = "redirect:/user/selectAll";
			message = "삭제완료!";
		}
		else {
			path = "redirect:/user/select/" + userNo;
			message = "삭제실패...";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	@GetMapping("insert")
	public String insertUser(	) {
		
		return "user/insertUser";
	}
	
	@PostMapping("insert")
	public String insertUser(
			@ModelAttribute UserDto user,
			RedirectAttributes ra){
		
		int result = service.insertUser(user);
		
		//
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = user.getUserId() + "님이 추가 되었습니다.";
			path = "redirect:/user/selectAll";
		}else {
			message = "추가 실패";
			path = "redirect:/user/insert";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
}
