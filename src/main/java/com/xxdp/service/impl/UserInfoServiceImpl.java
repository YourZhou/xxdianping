package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.entity.UserInfo;
import com.xxdp.mapper.UserInfoMapper;
import com.xxdp.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
* @author YourZhou
* @description 针对表【tb_user_info】的数据库操作Service实现
* @createDate 2023-04-03 20:24:27
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




