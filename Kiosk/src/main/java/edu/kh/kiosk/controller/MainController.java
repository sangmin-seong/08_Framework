package edu.kh.kiosk.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.kiosk.dto.Kiosk;
import edu.kh.kiosk.service.KioskService;

@Controller
public class MainController {
	
	@Autowired
	private KioskService service;
	
	@RequestMapping("/")
	public String mainPage() {
		
		Map<String, Object> map = service.selectAll();
		
		
		
		
		
		
		
		return "common/main";
	}
	
}
