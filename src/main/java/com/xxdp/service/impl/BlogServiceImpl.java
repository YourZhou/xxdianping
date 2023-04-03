package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.entity.Blog;
import com.xxdp.mapper.BlogMapper;
import com.xxdp.service.BlogService;
import org.springframework.stereotype.Service;

/**
* @author YourZhou
* @description 针对表【tb_blog】的数据库操作Service实现
* @createDate 2023-04-03 20:24:26
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

}




