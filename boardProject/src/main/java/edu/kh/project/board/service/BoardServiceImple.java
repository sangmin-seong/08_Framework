package edu.kh.project.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Pagination;
import edu.kh.project.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImple implements BoardService{
		
	private final BoardMapper mapper;
	
	// 게시글 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		
		// 1. boardCode가 일치하는 게시글의 전체 개수 조회
		// (조건 : 삭제되지 않은 글만 카운트!!!!)
		int listCount = mapper.getListCount(boardCode);
		
		// 2. listCount와 cp를 이용해서
		//		조회될 목록 페이지,
		//		출력할 페이지네이션의 값을 계산할
		// 		Pagination 객체 생성하기
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. DB에서 cp(조회하려는 페이지)에 해당하는 행을 조회하는 코드 
		//  ex) cp == 1, 전체 목록 중 1~10행 결과만 반환
		//			cp == 2, 전체 목록 중 11~20행 결과만 반환
		//			cp == 10, 전체 목록 중 91~100행 결과만 반환
		
		/* [Rowbounds 객체]
		 * - Mybatis 제공 객체
		 * 
		 * - 지정된 크기(offset) 만큼 행을 건너뛰고
		 * 	 제한된 크기(limit) 만큼의 행을 조회함
		 * 
		 * - [사용법] : Mapper의 메서드 호출 시
		 * 							2번째 이후 매개변수로 전달
		 * 							(1번은 SQL에 전달할 파라미터가 기본값)
		 * */
		
		int limit = pagination.getLimit(); // 10
		int offset = (cp-1) * limit; 
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묶어서 반환
		Map<String, Object> map = new HashMap<>();
		map.put("boardList", boardList);
		map.put("pagination", pagination);

		// Map 생성 + 바로 데이터 담기
		// Map<String, Object> map2  
		// 	= Map.of("boradList", boardList, "pagination", pagination);
		
			
		return map;
	}
	
	// 게시글 상세 조회
	@Override
	public Board selectDetail(Map<String, Integer> map) {
		
		/* [boardNo처럼 하나의 값을 이용해 여러 번 SELECT 수행하는 경우]
		 *  
		 * 1. 하나의 Service메서드에서
		 * 		여러 Mapper 메서드 호출하기 
		 * 		
		 * 		service -> mapper(select 연속 수행) -> DB
		 * 		(필요한 SELECT 만큼 전체 반복)
		 * 
		 * 2. MyBatis에서 제공하는
		 * 		<resultMap>, <collection> 이용하기
		 * 
		 * 		service -> mapper(select 연속 수행) -> DB
		 */
		
		
		return mapper.selectDetail(map);
	}

	// 조회 수 1 증가
	@Override
	public int updateReadCount(int boardNo) {
		return mapper.updateReadCount(boardNo);
	}
	
	// 좋아요 체크 or 해제
	@Override
	public Map<String, Object> boardLike(int boardNo, int memberNo) {
		
		// 1. 좋아요 누른 적 있는지 검사
		int result = mapper.checkBoardLike(boardNo, memberNo);
		
		// 2. 좋아요 여부에 따라 INSERT / DELETE mapper 호출
		int result2 = 0;
		if(result == 0) {
			result2 = mapper.insertBoardLike(boardNo, memberNo);
		}else {
			result2 = mapper.deleteBoardLike(boardNo, memberNo);
		}
		
		// 3. insert / delete 성공 시 해당 게시글의 좋아요 개수 조회
		int count = 0;
		if(result2 > 0) {
			count = mapper.getLikeCount(boardNo);
		}else {
			return null; // insert, delete 실패 시
		}
		
		// 4. 좋아요 결과를 Map에 저장해서 반환 
		Map<String, Object> map = new HashMap<>();
		
		map.put("count", count);
		
		if(result == 0) map.put("check", "insert");
		else						map.put("check", "delete");
		
		
		return map;
	}
	
	// DB에서 모든 게시판 종류를 조회
	@Override
	public List<Map<String, String>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
	}
}
