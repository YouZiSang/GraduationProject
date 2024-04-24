package com.example.yzs.service.impl.shoppingcart;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.dao.BookDao;
import com.example.yzs.dao.shoppingcart.ShoppingCartDao;
import com.example.yzs.entity.Books;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.shoppingcart.ShoppingCart;
import com.example.yzs.exception.CustomException;
import com.example.yzs.service.shoppingcart.ShoppingCartService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartDao shoppingCartDao;
    private Books books;

    private final BookDao bookDao;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao, BookDao bookDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.bookDao = bookDao;
    }


    @Override
    public void addProducts(ShoppingCart shoppingCart, Integer shopperId) {
        String ISBN = shoppingCart.getISBN();
        shoppingCart.setGoodsId(shoppingCart.getId());
        shoppingCart.setShopperId(shopperId);

        ShoppingCart existingCart = shoppingCartDao.findByISBN(ISBN, shopperId);
        if (existingCart == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setTotalPrice(shoppingCart.getPrice());
            shoppingCartDao.addProducts(shoppingCart);
        } else {
            existingCart.setNumber(existingCart.getNumber() + 1);
            BigDecimal number = new BigDecimal(existingCart.getNumber());
            existingCart.setTotalPrice(shoppingCart.getPrice().multiply(number));
            shoppingCartDao.updateShoppingCart(existingCart);
        }


    }

    @Override
    public List<ShoppingCart> shoppingCartList(Integer shopperId) {
        return shoppingCartDao.selectShoppingCartList(shopperId);
    }

    @Override
    public void batchDeleteBooks(Integer shopperId, List<ShoppingCart> selectedItems) {
        shoppingCartDao.batchDelete(shopperId, selectedItems);
    }

    @Override
    public void deleteBooks(Integer shopperId, String ISBN) {
        shoppingCartDao.delete(shopperId, ISBN);
    }

    @Override
    public Map<String, Object> selectBooksNumber(Integer shopperId) {
        return shoppingCartDao.selectBookNumber(shopperId);
    }


    @Override
    public void updateBooksNumber(ShoppingCart shoppingCart, Integer shopperId) {
//        shoppingCart.setNumber(shoppingCart.getNumber()-1);
        shoppingCart.setShopperId(shopperId);
        shoppingCart.setISBN(shoppingCart.getISBN());
        if (shoppingCart.getNumber() == 1) {
            shoppingCartDao.delete(shopperId, shoppingCart.getISBN());
        }
        shoppingCartDao.updateNumber(shoppingCart);
    }

}
