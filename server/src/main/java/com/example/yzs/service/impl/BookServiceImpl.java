package com.example.yzs.service.impl;

import com.example.yzs.dao.BookDao;
import com.example.yzs.entity.Books;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.evaluation.BookEvaluation;
import com.example.yzs.service.BookService;
import com.example.yzs.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public PageBean<Books> bookList(Integer currentPage, Integer pageSize) {
        System.out.println(pageSize);
        //1,创建PageBean对象
        PageBean<Books> pageBean = new PageBean<>();
        //2.开启分页查询PageHelp
        PageHelper.startPage(currentPage, pageSize);
        //3.调用mapper
        List<Books> list = bookDao.bookList();
        //page中提供了方法，可以获取PageHelp分页查询后，得到的总记录条数和当前页数据
        Page<Books> bookPage = (Page<Books>) list;
        //把数据填充到PageBean对象中
        pageBean.setTotal(bookPage.getTotal());
        pageBean.setItems(bookPage.getResult());
        return pageBean;
    }

    @Override
    public void updateBooks(Books books) {
        if (books.getNumber() > 1) {
            books.setNumber(books.getNumber() - 1);
            bookDao.updateBooks(books);
        }

    }


    @Override
    public void addCollection(Books book) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer collectorsId = (Integer) map.get("uid");
        String role = "用户";

        if (collectorsId == null) {
            collectorsId = (Integer) map.get("cid");
            role = "管理员";
        }

        book.setCollectorsId(collectorsId);
        book.setRole(role);
        bookDao.addCollection(book);
    }

    @Override
    public List<Books> findCollectedBooks(Integer collectorsId, List<String> ISBN) {
        return bookDao.findCollectedBooks(collectorsId, ISBN);
    }


    @Override
    public void deleteCollectedBooks(Integer id, String ISBN) {
        bookDao.deleteCollectedBooks(id, ISBN);
    }

    @Override
    public void updateBookSale(Integer goodsId, Integer number) {
        bookDao.updateBookSale(goodsId, number);
    }

    @Override
    public List<BookEvaluation> getEvaluation(Integer id) {

        return bookDao.getEvaluation(id);
    }


    @Override
    public List<Books> getRelatedBooks(String type, String ISBN) {
        return bookDao.getRelatedBooks(type, ISBN);
    }
}
