package edu.kh.semiProject.member.dto;

import edu.kh.semiProject.member.dto.Member;
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
@Builder // 빌더 패턴 : 객체 생성 + 초기화를 쉽게하는 패턴
public class Member {
	
	private int memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String enrollDate;
	private String mamberDelFl;
	private int autority;
}

