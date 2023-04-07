package com.xxdp.service;

import com.xxdp.dto.Result;
import com.xxdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author YourZhou
* @description 针对表【tb_blog】的数据库操作Service
* @createDate 2023-04-03 20:24:26
*/
public interface BlogService extends IService<Blog> {

    Result saveBlog(Blog blog);

    Result likeBlog(Long id);

    Result queryHotBlog(Integer current);

    Result queryBlogById(Long id);

    Result queryBlogLikes(Long id);

    Result queryBlogOfFollow(Long max, Integer offset);
}
