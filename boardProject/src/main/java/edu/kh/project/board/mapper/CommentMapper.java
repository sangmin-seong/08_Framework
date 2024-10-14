package edu.kh.project.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.board.dto.Comment;

@Mapper
public interface CommentMapper {
	
	/** 댓글 작성
	 * @param comment
	 * @return commentNo
	 */
	int commentInsert(Comment comment);

	
	/** 댓글 삭제
	 * @param commentNo
	 * @param memberNo
	 * @return result
	 */
	int commentDelete(
			@Param("commentNo") int commentNo,
			@Param("memberNo") int memberNo);


	/** 댓글 수정
	 * @param comment
	 * @return
	 */
	int commentUpdate(Comment comment);

}
