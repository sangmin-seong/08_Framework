package edu.kh.project.common.util;

import java.text.SimpleDateFormat;

public class FileUtil {
	
	// 파일명 뒤에 붙는 숫자 (1~99999 반복)
	public static int seqNum = 1;

	/** 전달 받은 원본 파일명을 시간형태의 파일명으로 변경
	 * 
	 * apple.jpg -> 20240927093712_00001.jpg
	 * @param OriginalFileName
	 * @return rename
	 */
	public static String rename(String originalFileName) {
		
		// 1) 확장자 추출하기 (.jpg)
		
		// 원본 파일명 뒤에서 부터 검색해서 처음 찾은 "."의 index
		int index = originalFileName.lastIndexOf(".");
		
		// 원본파일명 "." 부터 끝까지 잘라낸 문자열 == .확장자
		String ext = originalFileName.substring(index);
		
		// 2) 현재 시간을 "yyyyMMddHHmmss" 형태의 문자열로 반환받기

		// SimpleDateFormatl :
		// 시간을 지정된 패턴 문자열로 간단히 바꾸주는 객체 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// new java.util.Date() : 현재 시간을 저장한 객체 생성
		String time = sdf.format(new java.util.Date());
		
		// 3) seqNum을 이용한 숫자 생성
		
		// %05d : 정수가 들어갈 5칸 짜리 오른쪽 정렬 패턴,
		//				빈칸엔 0을 채움
		String number = String.format("%05d", seqNum);
		
		seqNum++;
		
		if(seqNum == 10000) seqNum = 1;
		
		return time + "_" + number + ext;
	}
	
}
