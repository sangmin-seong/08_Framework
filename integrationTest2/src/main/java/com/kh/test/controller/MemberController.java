package com.kh.test.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.test.dto.Member;
import com.kh.test.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {
	
	private final MemberService service;
	// 의존성 주입을 위해서 @RequiredArgsContstructor 사용하여
	// private final MemberService service로 사용 하거나
	// @Autowired를 사용 하여 의존성을 주입하여야 한다
	
	@GetMapping("selectAllList")
	@ResponseBody 
	// forward가 아닌 service에서 받아온 값을 전달하기 위해서는
	// @ResponseBody 어노테이션을 이용하여 값을 그대로 전달해 주어야함.
	public List<Member> selectAllList() {
		return service.selectAllList();
	}
	
}