package com.groupb.cuiz.web.board.notice;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.groupb.cuiz.web.board.BoardDAO;
import com.groupb.cuiz.web.board.BoardDTO;
import com.groupb.cuiz.web.board.BoardFileDTO;
import com.groupb.cuiz.web.board.BoardService;
import com.groupb.cuiz.support.util.file.FileManager;
import com.groupb.cuiz.support.util.pager.Pager;

@Service
public class NoticeService implements BoardService{
	
	@Autowired
	private NoticeDAO boardDAO;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private ServletContext servletContext;
	
	//list
	@Override
	public List<BoardDTO> getList(Pager pager)throws Exception{
		pager.makeRow();
		
		pager.makeNum(boardDAO.getTotalCount(pager));
		
		return boardDAO.getList(pager);
		
	}

	//add
	@Override
	public int getAdd(BoardDTO boardDTO, MultipartFile[] attchs) throws Exception {
		// TODO Auto-generated method stub
		
		//글 번호 소환
		int result = boardDAO.getAdd(boardDTO);
		
		//경로
		String path = servletContext.getRealPath("/resources/upload/notice");
		
		//파일명 소환
		for(MultipartFile f: attchs) {
			if (f.isEmpty()) {
				continue;
			}
			String file_Name = fileManager.fileSave(path,f);
			
			//db에 저장
			BoardFileDTO boardFileDTO = new BoardFileDTO();
			boardFileDTO.setFile_Name(file_Name);
			boardFileDTO.setOri_Name(file_Name);
			boardFileDTO.setFile_Num(boardDTO.getBoard_Num());
			result = boardDAO.getFileAdd(boardFileDTO);
		}
		
		return result;
		
	}

	//detail
	@Override
	public BoardDTO getDetail(BoardDTO boardDTO) throws Exception {
		// TODO Auto-generated method stub
		return boardDAO.getDetail(boardDTO);
	}

	//update
	@Override
	public int getUpdate(BoardDTO boardDTO, MultipartFile[] attchs) throws Exception {
		// TODO Auto-generated method stub
		return boardDAO.getUpdate(boardDTO);
	}

	//delete
	@Override
	public int getDelete(BoardDTO boardDTO) throws Exception {
		// TODO Auto-generated method stub
		List<BoardFileDTO> ar = boardDAO.getFileList(boardDTO);
		String path = servletContext.getRealPath("/resources/upload/notice");
		for(BoardFileDTO b: ar) {
			fileManager.fileDelete(path, b.getFile_Name());
		}
		int result = boardDAO.getDelete(boardDTO);
		return 0;
	}
	

}
