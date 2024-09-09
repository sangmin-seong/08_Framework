package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.dto.Todo;
import edu.kh.todo.service.TodoListService;
import oracle.jdbc.proxy.annotation.Post;

@RequestMapping("todo")
@Controller
public class TodoListController {
	
	@Autowired
	private TodoListService service;
	
	
	
	@PostMapping("add")
	public String todoAdd(
			@ModelAttribute Todo todo,
			RedirectAttributes ra) {
		
		int result = service.todoAdd(todo);
		
		String message = null;
		
		if(result > 0)  message = "할일 추가 성공";
		else            message = "할일 추가 실패";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/";
	}
	
	@GetMapping("detail/{todoNo}")
	public String todoDetail(
			@PathVariable("todoNo") int todoNo,
			Model model,
			RedirectAttributes ra
			) {
		Todo todo = service.todoDetail(todoNo);
		
		
		
		
		if(todo == null) { //조회 결과가 없을 경우
			
			ra.addFlashAttribute("message", "조회된 결과가 없습니다.");
			
			return "redirect:/";
		}
		
		model.addAttribute("todo", todo);
		
		return "todo/detail";
	}
	
	
	
	@GetMapping("complete")
	public String todoComplete(
			@RequestParam ("todoNo") int todoNo,
			RedirectAttributes ra
			) {
		
		int result = service.todoComplete(todoNo);
		
		String message = null;
		String path    = null;
		
		if(result > 0) {
			message = "변경 완료";
			path = "redirect:/todo/detail/"+todoNo;
		}
		else {
			message = "변경 실패";
			path = "redirect:/";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	@GetMapping("delete")
	public String todoDelete(
			@RequestParam("todoNo") int todoNo,
			Model model,
			RedirectAttributes ra
			) {
		
		
		int result = service.todoDelete(todoNo);
		
		String message = null;
		
		if(result > 0) message = "삭제 완료";
		else           message = "삭제 실패";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/";
	}
	
	@GetMapping("update")
	public String todoUpdate(
			@RequestParam ("todoNo") int todoNo,
			Model model,
			RedirectAttributes ra
			) {
		
		// 상세조회 서비스 호출
		Todo todo = service.todoDetail(todoNo);
		
		if(todo == null) { // todoNo 일치하는 할 일이 없을 경우
			ra.addFlashAttribute("message","할 일이 존재하지 않습니다.");
			return "redirect:/";
		}

		model.addAttribute("todo", todo);
		
		return "todo/update"; // 수정 화면
	}
	
	@PostMapping("update")
	public String todoUpdate(
			Todo todo,
			RedirectAttributes ra
			) {
	
		int result = service.todoUpdate(todo);
		
		String message = null;
		String path    = null;
		
		if(result > 0) {
			message = "변경완료";
			path = "redirect:/todo/detail/"+todo.getTodoNo();
		}else {
			message = "변경실패";
			path = "redirect:/todo/update?todoNo" + todo.getTodoNo();
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
}
