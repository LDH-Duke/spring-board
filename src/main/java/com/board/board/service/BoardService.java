package com.board.board.service;

import com.board.board.entity.Board;
import com.board.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired // spring bean이 알아서 읽어와서 주입해줌 Defendence Injection
    private BoardRepository boardRepository;
    //현재 BoardRepository class는 interface이기에 구현체가 필요하다. 그래서 new BoardRepository로 객체를 생성해야함


    /**
     * 게시물 저장
     */
    public void write(Board board){
        boardRepository.save(board);

    }


    /**
     * 게시물 조회
     */
    public List<Board> search(){
        return boardRepository.findAll();
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



}
