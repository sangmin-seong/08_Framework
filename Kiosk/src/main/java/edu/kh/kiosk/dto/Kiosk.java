package edu.kh.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Kiosk {
	
	private int menuNo;
	private String menuTitle;
	private String menuDetail; 
	private int menuPrice;
	private int menuResidual;
	private String enrollDate;
}
