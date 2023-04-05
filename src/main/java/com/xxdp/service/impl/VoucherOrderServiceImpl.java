package com.xxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxdp.dto.Result;
import com.xxdp.entity.VoucherOrder;
import com.xxdp.service.SeckillVoucherService;
import com.xxdp.service.VoucherOrderService;
import com.xxdp.mapper.VoucherOrderMapper;
import com.xxdp.utils.RedisIdWorker;
import com.xxdp.utils.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author YourZhou
 * @description 针对表【tb_voucher_order】的数据库操作Service实现
 * @createDate 2023-04-03 20:24:27
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder>
        implements VoucherOrderService {

    @Resource
    private SeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable{

        @Override
        public void run() {

        }
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");
        // 1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString(),
                String.valueOf(orderId)
        );
        assert result != null;
        int r = result.intValue();
        // 2.判断结果是否为0
        if (r != 0) {
            // 2.1.不为0 ，代表没有购买资格
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }
        // 7.返回订单id
        return Result.ok(orderId);
    }


    public void createVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        Long voucherId = voucherOrder.getVoucherId();
        // 创建锁对象
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        // 尝试获取锁
        boolean isLock = redisLock.tryLock();
        // 判断
        if (!isLock) {
            // 获取锁失败，直接返回失败或者重试
            log.error("不允许重复下单！");
            return;
        }

        try {
            // 5.1.查询订单
            int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            // 5.2.判断是否存在
            if (count > 0) {
                // 用户已经购买过了
                log.error("不允许重复下单！");
                return;
            }

            // 6.扣减库存
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1") // set stock = stock - 1
                    .eq("voucher_id", voucherId).gt("stock", 0) // where id = ? and stock > 0
                    .update();
            if (!success) {
                // 扣减失败
                log.error("库存不足！");
                return;
            }

            // 7.创建订单
            save(voucherOrder);
        } finally {
            // 释放锁
            redisLock.unlock();
        }
    }


    /**
     private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);
     private class VoucherOrderHandler implements Runnable{

    @Override public void run() {
    while (true){
    try {
    // 1.获取队列中的订单信息
    VoucherOrder voucherOrder = orderTasks.take();
    // 2.创建订单
    createVoucherOrder(voucherOrder);
    } catch (InterruptedException e) {
    log.error("处理订单异常", e);
    }
    }
    }
    }
     **/

    /**
     public Result seckillVoucher(Long voucherId) {
     Long userId = UserHolder.getUser().getId();
     // 1.执行lua脚本
     Long result = stringRedisTemplate.execute(
     SECKILL_SCRIPT,
     Collections.emptyList(),
     voucherId.toString(),
     userId.toString()
     );
     assert result != null;
     int r = result.intValue();
     // 2.判断结果是否为0
     if (r != 0) {
     // 2.1.不为0 ，代表没有购买资格
     return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
     }
     // 7.创建订单
     VoucherOrder voucherOrder = new VoucherOrder();
     // 7.1.订单id
     long orderId = redisIdWorker.nextId("order");
     voucherOrder.setId(orderId);
     // 7.2.用户id
     voucherOrder.setUserId(userId);
     // 7.3.代金券id
     voucherOrder.setVoucherId(voucherId);


     // 7.返回订单id
     return Result.ok(orderId);
     }
     **/

    /**
     public Result seckillVoucher(Long voucherId) {
     // 1.查询优惠券
     SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
     // 2.判断秒杀是否开始
     if (voucher.getBeginTime().isAfter((LocalDateTime.now()))){
     // 尚未开始
     return Result.fail("秒杀尚未开始！");
     }
     // 3.判断秒杀是否已经结束
     if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
     // 已经结束
     return Result.fail("秒杀已经结束！");
     }
     // 4.判断库存是否充足
     if (voucher.getStock()<1) {
     // 库存不足
     return Result.fail("库存不足！");
     }

     return this.createVoucherOrder(voucherId);
     }
     **/

    /**
     public Result createVoucherOrder(Long voucherId) {
     // 5.一人一单
     Long userId = UserHolder.getUser().getId();
     // 创建锁对象
     SimpleRedisLock redisLock = new SimpleRedisLock("order:" + voucherId, stringRedisTemplate);
     // 尝试获取锁
     boolean isLock = redisLock.tryLock(1200L);
     // 判断
     if (!isLock) {
     // 获取锁失败，直接返回失败或者重试
     return Result.fail("不允许重复下单！");
     }

     try {
     // 5.1.查询订单
     int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
     // 5.2.判断是否存在
     if (count > 0) {
     // 用户已经购买过了
     return Result.fail("用户已经购买过一次！");
     }

     // 6.扣减库存
     boolean success = seckillVoucherService.update()
     .setSql("stock = stock - 1") // set stock = stock - 1
     .eq("voucher_id", voucherId).gt("stock", 0) // where id = ? and stock > 0
     .update();
     if (!success) {
     // 扣减失败
     return Result.fail("库存不足！");
     }

     // 7.创建订单
     VoucherOrder voucherOrder = new VoucherOrder();
     // 7.1.订单id
     long orderId = redisIdWorker.nextId("order");
     voucherOrder.setId(orderId);
     // 7.2.用户id
     voucherOrder.setUserId(userId);
     // 7.3.代金券id
     voucherOrder.setVoucherId(voucherId);
     save(voucherOrder);

     // 7.返回订单id
     return Result.ok(orderId);
     } finally {
     // 释放锁
     redisLock.unlock();
     }
     }
     **/

    /**
     public Result createVoucherOrder(Long voucherId) {
     // 5.一人一单
     Long userId = UserHolder.getUser().getId();
     synchronized (userId.toString().intern()){
     // 5.1.查询订单
     Integer count = this.query().eq("user_id", userId).eq("voucher_id", voucherId).count();
     // 5.2.判断是否存在
     if (count>0){
     // 用户已经购买过了
     return Result.fail("用户已经购买过一次！");
     }
     // 6.扣减库存
     boolean isSuccess = seckillVoucherService.update()
     .setSql("stock = stock - 1")
     .eq("voucher_id", voucherId)
     .gt("stock", 0)
     .update();
     if (!isSuccess) {
     // 扣减失败
     return Result.fail("库存不足！");
     }
     // 7.创建订单
     VoucherOrder voucherOrder = new VoucherOrder();
     // 7.1.订单id
     Long orderId = redisIdWorker.nextId("order");
     voucherOrder.setId(orderId);
     // 7.2.用户id
     voucherOrder.setUserId(userId);
     // 7.3.代金券id
     voucherOrder.setVoucherId(voucherId);
     this.save(voucherOrder);
     // 7.返回订单id
     return Result.ok(orderId);
     }
     }**/
}




