package com.example.yzs.service;

import com.example.yzs.entity.Books;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.evaluation.BookEvaluation;

import java.util.List;

public interface BookService {

    PageBean<Books> bookList(Integer currentPage, Integer pageSize);

    void updateBooks(Books books);

    void addCollection(Books book);

    List<Books> findCollectedBooks(Integer collectorsId, List<String> ISBN);

    void deleteCollectedBooks(Integer id, String ISBN);

    void updateBookSale(Integer goodsId,Integer number);

    List<BookEvaluation> getEvaluation(Integer id);

    List<Books> getRelatedBooks(String type, String ISBN);
}
