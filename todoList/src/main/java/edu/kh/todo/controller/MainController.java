package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.dto.Todo;
import edu.kh.todo.service.TodoListService;

@Controller
public class MainController {
	
	@Autowired
	private TodoListService service;
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		
		Map<String, Object> map = service.selectTodoList();
		
		// map에 담긴 값 꺼내 놓기
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		
		int completeCount = (int)map.get("completeCount");
		
		
		// 조회결과 request scope에 추가
		model.addAttribute("todoList", todoList);
		
		model.addAttribute("completeCount", completeCount);
		
		
		return "common/main";
	}
	
	
	
	
}
