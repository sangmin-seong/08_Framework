package edu.kh.kiosk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.kiosk.mapper.KioskMapper;

@Service
public class KioskServiceImpl {

	@Autowired
	private KioskMapper mapper;
	
	
	
}
