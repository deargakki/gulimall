package com.atguigu.gulimall.auth.feign;


import com.atguigu.common.utils.R;
import com.atguigu.gulimall.auth.vo.MemberRegistVo;
import com.atguigu.gulimall.auth.vo.SocialUser;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {
    //注册
    @PostMapping("/member/member/regist")
    R regist(@RequestBody MemberRegistVo vo);

//    登录
    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    //社交
    //社交登录
    @PostMapping("/member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser vo) throws Exception;

}
