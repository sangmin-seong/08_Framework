package edu.kh.project.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.BoardImg;
import edu.kh.project.member.dto.Member;

@Mapper
public interface EditBoardMapper {
	
	// 게시판 작성
	int boardInsert(Board inputBoard);

	/** 여러 이미지 한번에 insert
	 * @param uploadList
	 * @return insertRows
	 */
	int insertUploadList(List<BoardImg> uploadList);
	
}
