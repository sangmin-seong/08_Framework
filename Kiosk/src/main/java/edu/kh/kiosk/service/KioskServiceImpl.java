package edu.kh.kiosk.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.kiosk.dto.Kiosk;
import edu.kh.kiosk.mapper.KioskMapper;

@Service
public class KioskServiceImpl implements KioskService{

	@Autowired
	private KioskMapper mapper;
	
	@Override
	public Map<String, Object> selectAll() {
		
		List<Kiosk> kioskList  = mapper.selectAll();
		
		
		kioskList.add(kiosk);
		
		return kioskList;
	}
}
