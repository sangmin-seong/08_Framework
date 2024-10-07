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



}
