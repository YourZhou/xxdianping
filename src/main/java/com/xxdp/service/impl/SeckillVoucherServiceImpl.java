package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.entity.SeckillVoucher;
import com.xxdp.service.SeckillVoucherService;
import com.xxdp.mapper.SeckillVoucherMapper;
import org.springframework.stereotype.Service;

/**
* @author YourZhou
* @description 针对表【tb_seckill_voucher(秒杀优惠券表，与优惠券是一对一关系)】的数据库操作Service实现
* @createDate 2023-04-04 23:00:52
*/
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher>
    implements SeckillVoucherService{

}




