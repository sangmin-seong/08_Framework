package edu.kh.project.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.dto.Board;
import edu.kh.project.member.dto.Member;

public interface EditBoardService {
	
	/** 게시판 작성
	 * @param inputBoard
	 * @param images
	 * @param loginMember
	 * @return
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images);

	/** 게시판 글 삭제
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int boardDelete(int boardNo, int memberNo);

	
	/** 게시글 수정
	 * @param inputBoard
	 * @param images
	 * @param deleteOrderList
	 * @return result
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList);



}
