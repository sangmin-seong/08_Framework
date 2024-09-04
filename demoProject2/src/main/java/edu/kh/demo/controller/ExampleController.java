package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RequestMapping("/example")
@Controller
public class ExampleController {
	
	// Servlet/JSP에 존재하는 내장 객체 4종류 데이터 유지 범위
	// 1) page : 해당 페이지만
	// 2) request : 요청받은 페이지, 위임된 곳까지
	// 3) session : 브라우져가 켜지고, 꺼질 때 까지
	// 4) application : 서버에 저장, 계속 쭉 ~

	
	
	
	
	/* Model
   * - org.springframework.ui 패키지
	 * 
	 * - Spring에서 데이터를 전달하는 역할의 객체
	 * 
	 * - 데이터 유지 범위(scope) : 기본 request
	 * 
	 * - @SessionAttributes 와 함께 사용하면 session scope로 변경
	 * 
	 * 
	 * [Model을 이용해서 값을 세팅하는 방법]
	 * 
	 * Model.addAttribute("key", value);
	 */
	
	
	
	@GetMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
		
		// request scope에 값 세팅
		req.setAttribute("test1", "HttpServletRequest로 세팅한 값");

		// model을 이용, request scope에 값 세팅
		model.addAttribute("test2","Model로 세팅한 값" );
		
		// 단일 값 세팅(숫자, 문자열)
		model.addAttribute("productName", "아이스 아메리카노");
		model.addAttribute("price", 2000);
		
		// 복수 값 세팅(배열, List)
		List<String> fruitList = new ArrayList<>();
		fruitList.add("복숭아");
		fruitList.add("딸기");
		fruitList.add("수박");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList", fruitList);
		
		
		// DTO 객체를 만들어 model에 세팅
		Student std = Student.builder()
												 .StudentNo("1111")
												 .name("짱구")
												 .age(15)
												 .build(); // -> 필드 전체가 아닌 일부 초기화 사용하기 좋음
		// 이어쓰는 것을 메서드 체이닝, 현재 메서드와 같은 것을 빌더 패턴이라고 함
		
		log.debug("std:{}", std);
		
		model.addAttribute("std", std);
		
		List<String> hobbyList = new ArrayList<>();
		hobbyList.add("축구");
		hobbyList.add("독서");
		hobbyList.add("코딩 공부");

		Student std2 = Student.builder()
													.StudentNo("2222")
													.name("철수")
													.age(20)
													.hobbyList(hobbyList)
													.build();
		
		model.addAttribute("std2", std2);
		
		return "ex/result1";
	}
	
	/**
	 * 
	 * @param model : Spring에서 데이터를 전달하는 용도의 객체
	 *								(기본 scope : request)
	 * @return
	 */
	@PostMapping("ex2")
	public String ex2(Model model) {
		
		model.addAttribute("str", "<h1>테스트 중..... &times;</h1>");
		
		
		return "ex/result2";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
