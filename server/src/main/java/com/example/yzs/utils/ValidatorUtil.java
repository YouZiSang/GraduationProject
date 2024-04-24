package com.example.yzs.utils;


import com.example.yzs.constant.MessageConstant;
import com.example.yzs.entity.Account;
import com.example.yzs.entity.Books;
import com.example.yzs.entity.User;
import com.example.yzs.entity.UserInfo;
import com.example.yzs.exception.CustomException;

import java.math.BigDecimal;

public class ValidatorUtil {
    private ValidatorUtil() {
        // 隐藏默认构造函数

    }



    public static void loginVerify(Account account) {
        // 登录校验
        if (account.getUid() == null || "".equals(account.getUid())) {
            throw new CustomException(MessageConstant.ACCOUNT_CANNOT_EMPTY);
        }
        if (account.getPassword() == null || "".equals(account.getPassword())) {
            throw new CustomException(MessageConstant.PASSWORD_CANNOT_EMPTY);
        }
        Integer uid = account.getUid();
        String password = Md5Util.getMD5String(account.getPassword());
        account.setUid(uid);
        account.setPassword(password);
    }

    //注册密码校验
    public static void registerPasswordVerify(Account account) {
        if (account.getPassword() == null || account.getPassword().isEmpty()) {
            throw new CustomException(MessageConstant.REGISTER_FAIL);
        }
        if (account.getPassword().length() < 8 || account.getPassword().length() > 16) {
            throw new CustomException(MessageConstant.REGISTER_FAIL);
        }
        if (!account.getPassword().matches("^(?=.*\\d)(?=.*[a-zA-Z])(?!.*\\s).{8,16}$")) {
            throw new CustomException(MessageConstant.REGISTER_FAIL);
        }
    }

    //信息校验
    public static void phoneAndSexVerify(UserInfo userInfo) {
        /**
         * 对电话号码进行合法性判断
         * 为空?
         * 1开头？11位？
         * */
        if (userInfo.getPhone() == null) {
            throw new CustomException("电话");
        }
        String phoneStr = String.valueOf(userInfo.getPhone());
        if (!phoneStr.matches("^1[3456789]\\d{9}$")) {
            throw new CustomException("电话不为空");
        }
        if (userInfo.getAvatarUrl() == null || userInfo.getAvatarUrl().isEmpty()) {
            userInfo.setAvatarUrl("logo.png"); // 设置默认头像路径
        }
        if (userInfo.getSex() == null || userInfo.getSex().isEmpty()) {
            throw new CustomException(MessageConstant.SEX_CANNOT_EMPTY);
        }
    }


    //修改信息校验
    public static void nicknameAndEmailVerify(UserInfo userInfo) {
        if (userInfo.getNickname() == null || userInfo.getNickname().isEmpty()) {
            throw new CustomException(MessageConstant.NICKNAME_CANNOT_EMPTY);
        }
        String email = userInfo.getEmail();
        if (email == null) {
            throw new CustomException(MessageConstant.EMAIL_CANNOT_EMPTY);
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new CustomException(MessageConstant.EMAIL_FORMAT_ERROR);
        }
    }

    public static void booksInfoVerify(Books books) {
        verifyNotNull("书名", books.getTitle(), MessageConstant.BOOK_TITLE_CANNOT_EMPTY);
        verifyNoSpace("书名", books.getTitle(), MessageConstant.SPACES_CANNOT_BE_INCLUDED);
        verifyNotNull("ISBN", books.getISBN(), MessageConstant.BOOK_ISBN_CANNOT_EMPTY);
        verifyNoSpace("ISBN", books.getISBN(), MessageConstant.SPACES_CANNOT_BE_INCLUDED);
        verifyISBNFormat("ISBN", books.getISBN(), MessageConstant.BOOK_ISBN_ERROR);
        verifyNotNull("作者", books.getAuthor(), MessageConstant.BOOK_AUTHOR_CANNOT_EMPTY);
        verifyNoSpace("作者", books.getAuthor(), MessageConstant.SPACES_CANNOT_BE_INCLUDED);
        verifyNoSpace("出版社", books.getPublisher(), MessageConstant.SPACES_CANNOT_BE_INCLUDED);
        verifyNotNull("出版社", books.getPublisher(), MessageConstant.BOOK_PUBLISHER_CANNOT_EMPTY);
        verifyNumber(books.getNumber(), MessageConstant.BOOK_NUMBER_CANNOT_EMPTY);
        verifyPrice(books.getPrice(), MessageConstant.BOOK_PRICE_CANNOT_EMPTY, MessageConstant.BOOK_PRICE_ERROR);
    }


    private static void verifyNotNull(String fieldName, Object value, String errorMessage) {
        if (value == null) {
            throw new CustomException(String.format("%s: %s", fieldName, errorMessage));
        }
    }

    private static void verifyNoSpace(String fieldName, String value, String errorMessage) {
        if (value != null && value.contains(" ")) {
            throw new CustomException(String.format("%s: %s", fieldName, errorMessage));
        }
    }

    private static void verifyISBNFormat(String fieldName, String value, String errorMessage) {
        if (value != null && !value.matches("^\\d{13}$")) {
            throw new CustomException(String.format("%s: %s", fieldName, errorMessage));
        }
    }

    private static void verifyNumber(Integer number, String errorMessage) {
        if (number == null || number <= 0) {
            throw new CustomException(errorMessage);
        }
    }

    private static void verifyPrice(BigDecimal price, String emptyErrorMessage, String formatErrorMessage) {
        if (price == null) {
            throw new CustomException(emptyErrorMessage);
        }
        if (!isValidPriceFormat(price)) {
            throw new CustomException(formatErrorMessage);
        }
    }

    private static boolean isValidPriceFormat(BigDecimal price) {
        if (price == null) {
            return false;
        }
        String priceStr = price.toString();
        String pricePattern = "^([1-9]\\d*|0)(\\.\\d{1,2})?$";
        return priceStr.matches(pricePattern);
    }

}
