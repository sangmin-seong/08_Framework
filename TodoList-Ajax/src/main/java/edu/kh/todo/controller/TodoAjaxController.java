package edu.kh.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todo.dto.Todo;
import edu.kh.todo.service.TodoListService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


/* @RequestBody
 * - 비동기 요청(ajax) 시 전달되는 데이터 중
 *   body 부분에 포함된 요청 데이터를
 *   알맞은 Java 객체 타입으로 바인딩하는 어노테이션
 * 
 * (쉬운 설명)
 * - 비동기 요청 시 body에 담긴 값을
 *   알맞은 타입으로 변환해서 매개 변수에 저장
 * */

/* @ResponseBody
 * - 컨트롤러 메서드의 반환 값을
 *   HTTP 응답 본문에 직접 바인딩하는 역할임을 명시
 *  
 * (쉬운 해석)  
 * -> 컨트롤러 메서드의 반환 값을
 *  비동기 (ajax)요청했던 
 *  HTML/JS 파일 부분에 값을 돌려 보낼 것이다를 명시
 *  
 *  - forward/redirect 로 인식 X
 * */

/* [HttpMessageConverter]
 *  Spring에서 비동기 통신 시
 * - 전달되는 데이터의 자료형
 * - 응답하는 데이터의 자료형
 * 위 두가지 알맞은 형태로 가공(변환)해주는 객체
 *    (Java)          (JS)
 * - 문자열, 숫자 <-> TEXT
 * - Map 					<-> JSON
 * - DTO 					<-> JSON
 * 
 * (참고)
 * HttpMessageConverter가 동작하기 위해서는
 * Jackson-data-bind 라이브러리가 필요한데
 * Spring Boot 모듈에 내장되어 있음
 * (Jackson : 자바에서 JSON 다루는 방법 제공하는 라이브러리)
 */

@Slf4j
@Controller
@RequestMapping("todo")
public class TodoAjaxController {

	@Autowired
	private TodoListService service;
	
	/** 1) 비동기로 할 일 추가
	 * @param todo : @RequestBody를 이용해서
	 * 				전달 받은 JSON 형태(문자열, String)의 데이터를 
	 * 				Todo 객체로 변환
	 * @return 
	 */
	@PostMapping("add")
	@ResponseBody
	public int todoAdd(@RequestBody Todo todo) {
		// 반환형을 알맞은 형태로 변경!!

		log.debug("todo : {}", todo);
		
		int result = service.todoAdd(todo);
		
		
		/* 비동기 통신의 목적 :
		 * "값" 또는 화면 일부만 갱신 없이
		 * 서버로 부터 응답 받고 싶을 때 사용
		 * */
		return result; // service 수행결과 그대로 반환
	}
	
	/** 
	 * @param todoNo : GET방식 요청은 body가 아닌
	 * 	주소에 담겨 전달된 "파라미터" -> @RequestParam으로 얻어옴 
	 * @return 검색된 제목
	 */
	@GetMapping("searchTitle")
	@ResponseBody // 비동기 요청한 JS 본문으로 값 반환
	public String SearchTitle(@RequestParam("todoNo") int todoNo) {
		
		String todoTitle = service.searchTitle(todoNo);
		
		// 서비스 결과를 "값"형태 그대로 JS 본문을 반환
		return todoTitle;
	}
	
	/** 전체 할 일 개수 조회
	 * @return 전체 할 일 개수
	 */
	@ResponseBody // 반환 값을 요청한 JS 코드로 그대로 돌려보내라
	@GetMapping("totalCount")
	public int getTotalCount() {
		return service.getTotalCount();
	}
	
	
	
	/** 완료된 할 일 개수 조회
	 * @return completeCount
	 */
	@ResponseBody // 호출한 ajax 코드로 값 자체를 반환 (forward X) 
	@GetMapping("completeCount") 
	public int getCompleteCount() {
		return service.getCompleteCount();
	}
	
	@ResponseBody
	@GetMapping("todoDetail")
	public Todo todoDetail(@RequestParam("todoNo") int todoNo) {
		
		// 반환형 String인 경우
		// - Java 객체는 JS에서 호환X
	  // -> Java에서 JS에 호환될 수 있도록 JSON 형태 데이터를 반환
		
		// return "{\"todoNo\":10, \"todoTitle\" : \"제목 테스트\"}";
		
		/* 반환형 Todo(String이 아닌 Object) */
		// -> Java 객체가 반환되면 JS에서 쓸수 없음
		//   당연히 Spring이 인식하므로
		//   자동으로 변환해 줄
		//	 "HttpMessageConverter" 객체가 변환해준다!
		return service.todoDetail(todoNo);
	}
	
	/**
	 * 할일 전체 목록 비동기 요청 처리
	 * @return
	 */
	@ResponseBody // 응답 데이터를 그대로 호출한 Ajax 코드로 반환
	@GetMapping("todoList")
	public List<Todo> getTodoList() {
		return service.getTodoList();
		
		// 비동기 요청에 대한 응답으로 객체 반환 시,
		// "HttpMessageConverter" 가 
		// JSON(단일 객체) 또는 JSONArray(배열, 컬렉션) 형태로 변환
		
		// "[{"K":V},{"K":V},{"K":V},....]" == JSONArray
	}
	
	/** 할 일 상세 조회
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("detail/{todoNo}")
	public Todo selectTodo(@PathVariable("todoNo") int todoNo) {
		return service.todoDetail(todoNo);
	}
	
	@ResponseBody
	@PutMapping("todoComplete")
	public int todoComplete(@RequestBody int todoNo) {
 		return service.todoComplete(todoNo);
	}
	
	@ResponseBody
	@DeleteMapping("todoDelete")
	public int todoDelete(@RequestBody int todoNo) {
		return service.todoDelete(todoNo);
	}
	
	
	@ResponseBody
	@PutMapping("todoUpdate")
	public int todoUpdate(@RequestBody Todo todo) {
		return service.todoUpdate(todo);
	}
}
