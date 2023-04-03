package com.xxdp.controller;

import com.xxdp.dto.LoginFormDTO;
import com.xxdp.dto.Result;
import com.xxdp.dto.UserDTO;
import com.xxdp.entity.UserInfo;
import com.xxdp.service.UserInfoService;
import com.xxdp.service.UserService;
import com.xxdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session){
        // 发送短信验证码并保存验证码
        return userService.sendCode(phone,session);
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm,HttpSession session){
        // 实现登录功能
        return userService.login(loginForm,session);
    }

    @GetMapping("/me")
    public Result me(){
        // 获取当前登录的用户并返回
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    /**
     * 登出功能
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(){
        UserHolder.remove();
        return Result.ok("注销成功");
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){
        UserInfo userInfo = userInfoService.getById(userId);
        if (userInfo ==null) {
            return Result.ok();
        }
        return Result.ok(userInfo);
    }
}
