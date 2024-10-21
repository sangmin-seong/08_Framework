package edu.kh.project.common.scheduling.service;

import java.util.List;

public interface SchedulingService {
	
	/** DB에 기록 된 모든 파일명 조회
	 * @return
	 */
	List<String> getDbFileNameList();

}
