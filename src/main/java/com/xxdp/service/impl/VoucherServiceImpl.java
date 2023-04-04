package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.dto.Result;
import com.xxdp.entity.SeckillVoucher;
import com.xxdp.entity.Voucher;
import com.xxdp.service.SeckillVoucherService;
import com.xxdp.service.VoucherService;
import com.xxdp.mapper.VoucherMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.xxdp.utils.RedisConstants.SECKILL_STOCK_KEY;

/**
* @author YourZhou
* @description 针对表【tb_voucher】的数据库操作Service实现
* @createDate 2023-04-03 20:24:27
*/
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher>
    implements VoucherService{

    @Resource
    private SeckillVoucherService seckillVoucherService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        save(voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
        // 保存秒杀库存到Redis中
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(),voucher.getStock().toString());
    }

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        List<Voucher> vouchers = this.getBaseMapper().queryVoucherOfShop(shopId);
        return Result.ok(vouchers);
    }
}




