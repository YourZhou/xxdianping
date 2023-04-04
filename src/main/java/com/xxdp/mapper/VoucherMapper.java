package com.xxdp.mapper;

import com.xxdp.entity.Voucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author YourZhou
* @description 针对表【tb_voucher】的数据库操作Mapper
* @createDate 2023-04-03 20:24:27
* @Entity xxdianping.entity.Voucher
*/
public interface VoucherMapper extends BaseMapper<Voucher> {

    List<Voucher> queryVoucherOfShop(@Param("shopId") Long shopId);
}




