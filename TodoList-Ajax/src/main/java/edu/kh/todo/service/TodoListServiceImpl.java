package edu.kh.todo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.dto.Todo;
import edu.kh.todo.mapper.TodoListMapper;

@Service
public class TodoListServiceImpl implements TodoListService{

	
	@Autowired
	private TodoListMapper mapper;
	
	@Override
	public Map<String, Object> selectTodoList() {
		
		List<Todo> todoList = mapper.selectTodoList();
		
		int completeCount = mapper.selectCompleteCount(); 
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
	}

	
	@Override
	@Transactional
	public int todoAdd(Todo todo) {
		
		return mapper.todoAdd(todo);
	}
	
	@Override
	public Todo todoDetail(int todoNo) {
		
		return mapper.todoDetail(todoNo);
	}
	
	
	
	@Override
	public int todoDelete(int todoNo) {
		return mapper.todoDelete(todoNo);
	}
	
	
	
	@Override
	public int todoComplete(int todoNo) {
		return mapper.todoComplete(todoNo);
	}
	
	
	@Override
	public int todoUpdate(Todo todo) {
		return mapper.todoUpdate(todo);
	}
	
	
	@Override
	public String searchTitle(int todoNo) {
		return mapper.searchTitle(todoNo);
	}
}
