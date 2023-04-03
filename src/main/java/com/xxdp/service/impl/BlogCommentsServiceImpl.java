package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.entity.BlogComments;
import com.xxdp.mapper.BlogCommentsMapper;
import com.xxdp.service.BlogCommentsService;
import org.springframework.stereotype.Service;

/**
* @author YourZhou
* @description 针对表【tb_blog_comments】的数据库操作Service实现
* @createDate 2023-04-03 20:24:27
*/
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments>
    implements BlogCommentsService {

}




