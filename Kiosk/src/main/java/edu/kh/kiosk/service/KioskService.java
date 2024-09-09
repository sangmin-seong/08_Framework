package edu.kh.kiosk.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.kh.kiosk.dto.Kiosk;

public interface KioskService {

	Map<String, Object> selectAll();

}
