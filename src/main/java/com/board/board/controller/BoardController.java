package com.board.board.controller;

import com.board.board.entity.Board;
import com.board.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

//    @GetMapping("/")
//    @ResponseBody //데이터를 응답 시 본문(body)에 담에서 전달하는 어노테이션
//    public String main(){
//        return "main";
//    }

    @GetMapping("/board/write")
    public String boardWriteForm(){
        return "boardWrite";
    }

    @PostMapping("/board/write")
    public String boardWrite(Board board, Model model, MultipartFile file) throws Exception{
        //public String boardWrite(@RequestParam(name = "title") String title, @RequestParam(name = "content")String content){
        //RequestParam Http 요청 파라미터의 이름으로 바인딩하여 그 값을 변수에 저장한다. http 파라미터와 이름이 동일하면 ()부분 생략가능
        //System.out.println("제목 :"+board.getTitle()+"내용 :"+board.getContent());

        boardService.write(board,file); // 게시물 저장 서비스

        model.addAttribute("message", "작성 완료");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name="searchkeyword", required = false) String searchKeyword){

        Page<Board> list = null;

        if(searchKeyword == null){
            list = boardService.search(pageable);
        }else {
            list = boardService.searchList(searchKeyword, pageable);
        }

        int currentPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(currentPage - 4, 1);
        int endPage = Math.min(currentPage + 5, list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardList";
    }

    @GetMapping("/board/detail")
    public String boardDetail(Model model, @RequestParam(name = "id") Integer id){

        model.addAttribute("detail", boardService.detail(id));

        return "boardDetail";
    }

    @GetMapping("/board/remove")
    public String boardRemove(@RequestParam(name = "id") Integer id){
        boardService.remove(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model){

        model.addAttribute("detail", boardService.detail(id));

        return "boardModify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdae(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception{

        //여기부터 12.08
        Board boardTemp = boardService.detail(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        model.addAttribute("message", "수정 완료");
        model.addAttribute("searchUrl", "/board/detail?id="+id);


        return "message";
    }

}
