package edu.kh.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.dto.Todo;

@Mapper
public interface TodoListMapper {

	List<Todo> selectTodoList();

	int selectCompleteCount();

	int todoAdd(Todo todo);

	Todo todoDetail(int todoNo);

	int todoDelete(int todoNo);

	int todoComplete(int todoNo);

	int todoUpdate(Todo todo);

	String searchTitle(int todoNo);

	
}
