package com.example.yzs.dao;

import com.example.yzs.entity.Books;
import com.example.yzs.entity.evaluation.BookEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookDao {
    List<Books> bookList();

    void updateBooks(Books books);

    void addCollection(Books book);

    List<Books> findCollectedBooks(@Param("collectorsId") Integer collectorsId, @Param("ISBN") List<String> ISBN);
    void deleteCollectedBooks(@Param("collectorsId") Integer collectors_id, @Param("ISBN") String ISBN);

    void updateBookSale(@Param("goodsId") Integer goodsId, @Param("number") Integer number);

    List<BookEvaluation> getEvaluation(Integer id);

    List<Books> getRelatedBooks(Integer id);

    List<Books> getRelatedBooks(@Param("type") String type, @Param("ISBN") String ISBN);
}
