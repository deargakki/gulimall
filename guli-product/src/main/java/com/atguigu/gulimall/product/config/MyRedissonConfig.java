package com.atguigu.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRedissonConfig {
    @Bean(destroyMethod ="shutdown")
    public RedissonClient redissonClient(){
        Config config = new Config();
        //设置redis的地址在哪
        config.useSingleServer().setAddress("redis://192.168.21.132:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
