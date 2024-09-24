package edu.kh.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.test.dto.Student;

@Controller // 1. 요청에 대해 응답하고 처리하는 Controller 임을 명시하고, 
						// 	 	Spring이 생성/관리하는 객체인 Bean으로 등록
@RequestMapping("student") // 2. 요청 주소에 대한 요청을 처리하는 메서드로
														//   /student/select라는 요청 주소에서 /student로 시작하는 요청을
														// 컨트롤러에 매핑하거나, @PostMapping("student/select"로 수정이 필요함
public class StudentController {

	@PostMapping("select") 
	public String selectStudent(Model model, @ModelAttribute Student student/* 커맨드 객체 생성 */) {
		
		
		
		model.addAttribute("stdName", student.getStdName());
		model.addAttribute("stdAge", student.getStdAge());
		model.addAttribute("stdAddress", student.getStdAddress());
		
		
		return "student/select";
	}
	
}