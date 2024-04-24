package com.example.yzs.service;


import com.example.yzs.entity.Account;
import com.example.yzs.entity.Questionnaire;

import java.util.List;

public interface UserService {

    Account login(Account account);

    Account findById(Integer id);

    void add(Account account);

    void updatePwd(Integer uid,String newPwd);

}
