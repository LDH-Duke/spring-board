package com.board.board.service;

import com.board.board.entity.Board;
import com.board.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired // spring bean이 알아서 읽어와서 주입해줌 Defendence Injection
    private BoardRepository boardRepository;
    //현재 BoardRepository class는 interface이기에 구현체가 필요하다. 그래서 new BoardRepository로 객체를 생성해야함


    /**
     * 게시물 저장
     */
    public void write(Board board, MultipartFile file) throws Exception{

        /*파일 저장 기본*/
        String path = System.getProperty("user.dir")+"src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(path, fileName);
        file.transferTo(saveFile);

        boardRepository.save(board);

    }


    /**
     * 게시물 조회
     */
    public Page<Board> search(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    /**
     * 게시물 상세 페이지
     */
    public Board detail(Integer id){
        //boardRepository.findById(id).get(); //findById가 optional 객체를 반환하는 것인데 이 구문처럼 바로 .get()하기보다 확실하게 값이 있는지 확인 한 후에 get해야함
        return boardRepository.findById(id).get();
    }

    /**
     * 특정 게시글 삭제
     */
    public void remove(Integer id){
        boardRepository.deleteById(id);
    }

    /**
     * 게시물 검색 기능
     */

    public Page<Board> searchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }





}