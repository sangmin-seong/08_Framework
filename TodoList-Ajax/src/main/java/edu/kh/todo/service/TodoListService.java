package edu.kh.todo.service;

import java.util.List;
import java.util.Map;

import edu.kh.todo.dto.Todo;

public interface TodoListService {

	/** 할일 목록 조회 + 완료된 할 일 개수
	 * @return
	 */
	Map<String, Object> selectTodoList();

	int todoAdd(Todo todo);

	Todo todoDetail(int todoNo);

	int todoDelete(int todoNo);

	int todoComplete(int todoNo);

	int todoUpdate(Todo todo);

	String searchTitle(int todoNo);

	int getTotalCount();

	int getCompleteCount();

	List<Todo> getTodoList();




}
