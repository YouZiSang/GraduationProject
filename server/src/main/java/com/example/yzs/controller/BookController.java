package com.example.yzs.controller;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.Books;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.evaluation.BookEvaluation;
import com.example.yzs.service.BookService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/list")
    public Result<PageBean<Books>> list(Integer currentPage, Integer pageSize) {
        PageBean<Books> pageBean = bookService.bookList(currentPage,pageSize);
        return Result.success(pageBean);
    }

    @PostMapping("updateBooksHouseBooks")
    public Result<String> updateBooks(@RequestBody Books books){

        bookService.updateBooks(books);
        return Result.success("图书数量修改成功");
    }

    @PostMapping("collection")
    public Result<String> addCollection(@RequestBody Books book){

        bookService.addCollection(book);
        return Result.success(MessageConstant.COLLECTION_SUCCESS);
    }

    @PostMapping("/findCollectedBooks")
    public Result<List> findCollectedBooks(@RequestBody List<String> ISBN){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer collectorsId = (Integer) map.get("uid");
        if (collectorsId == null){
            collectorsId = (Integer) map.get("cid");
        }
        List<Books> bookList = bookService.findCollectedBooks(collectorsId, ISBN);
        return Result.success(bookList);
    }

    @PostMapping("/deleteCollectedBooks")
    public Result<String> deleteCollectedBooks(@RequestBody String ISBN){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer collectorsId = (Integer) map.get("uid");
        if (collectorsId == null){
            collectorsId = (Integer) map.get("cid");
        }
        bookService.deleteCollectedBooks(collectorsId, ISBN);
        return Result.success(MessageConstant.CANCEL_COLLECTION);
    }

    @GetMapping("evaluation")
    public Result<List<BookEvaluation>> getBookEvaluation(@RequestParam("id") Integer id){

        List<BookEvaluation> bookEvaluation = bookService.getEvaluation(id);
        return Result.success(bookEvaluation);
    }

    @GetMapping("related")
    public Result<List<Books>> getRelatedBooks(@RequestParam String type,@RequestParam String ISBN){

        List<Books> relatedBooks = bookService.getRelatedBooks(type,ISBN);
        return Result.success(relatedBooks);
    }
}
