package edu.kh.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.dto.Todo;

@Mapper
public interface TodoListMapper {

	List<Todo> selectTodoList();

	int selectCompleteCount();

}
