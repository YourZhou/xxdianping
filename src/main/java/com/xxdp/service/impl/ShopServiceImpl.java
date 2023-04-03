package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.dto.Result;
import com.xxdp.entity.Shop;
import com.xxdp.mapper.ShopMapper;
import com.xxdp.service.ShopService;
import org.springframework.stereotype.Service;

/**
* @author YourZhou
* @description 针对表【tb_shop】的数据库操作Service实现
* @createDate 2023-04-03 20:24:27
*/
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
    implements ShopService{

    @Override
    public Result queryById(Long id) {
        return null;
    }

    @Override
    public Result update(Shop shop) {
        return null;
    }
}




