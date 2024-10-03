package com.kh.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	private int memberNo;
	private String name;
	private String address;
	private int    age;
	// member-mapper.xml에서 Member DTO로 받아올 때
	// 변수명이 같지 않아 자료를 받아올 수 없음.
	// 따라서 변수명을 위와 같이 수정해
	
	
	public class Member {

		private int memberNo;
		private String memberName;
		private String memberId;
		private String memberPassword;

		

		}
}