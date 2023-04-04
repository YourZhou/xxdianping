package com.xxdp.service;

import com.xxdp.dto.Result;
import com.xxdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author YourZhou
* @description 针对表【tb_voucher_order】的数据库操作Service
* @createDate 2023-04-03 20:24:27
*/
public interface VoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);
}
