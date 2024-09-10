package edu.kh.kiosk.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.kiosk.dto.Kiosk;

@Mapper
public interface KioskMapper {

	List<Kiosk> selectAll(); 

	
	
	

}
