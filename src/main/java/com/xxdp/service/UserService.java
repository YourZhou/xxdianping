package com.xxdp.service;

import com.xxdp.dto.LoginFormDTO;
import com.xxdp.dto.Result;
import com.xxdp.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
* @author YourZhou
* @description 针对表【tb_user】的数据库操作Service
* @createDate 2023-04-03 20:24:27
*/
public interface UserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();
}
