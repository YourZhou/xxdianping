package com.xxdp.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author YiZhou
 * @date 2023/4/4
 * @Description
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
