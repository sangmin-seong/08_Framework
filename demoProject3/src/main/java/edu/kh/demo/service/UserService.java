package edu.kh.demo.service;

import java.util.List;

import org.apache.catalina.User;

import edu.kh.demo.dto.UserDto;

public interface UserService {
	
	/* 사용자 번호와 일치하는 사용자 조
	 * 
	 */
	String selectUserName(int userNo);

	/** 사용자 전체 조회
	 * @return userList
	 */
	List<UserDto> selectAll();

	
	// userNo가 일치하는 사용자 조회
	UserDto selectUser(int userNo);

	int updateUser(UserDto user);

	int deleteUser(int userNo);

	int insertUser(UserDto user);

}
