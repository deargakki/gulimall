package com.atguigu.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberResVo;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.util.HttpUtils;
import com.atguigu.gulimall.auth.vo.GiteeUserInfo;
import com.atguigu.gulimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 孟享广
 * @date 2021-01-31 2:03 下午
 * @description 处理社交登录请求
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("client_id", "0f00add3737a1970f4575f8d9bb00ee79c03bede620aaf6b86f8825b6ebe17b5");//和login.html的要保持一致
        map.put("client_secret", "3d6150b2620ca35396171d70c494ce43093ed95d031c9f4a0e9cfaa82eb92cca");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code", code);
        //1 根据 code 换取 access_token 能获取则成功
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), map, new HashMap<>());

        //处理返回的 response 这个 json
        if (response.getStatusLine().getStatusCode() == 200) {
            //成功获取了access_token JSON逆转为对象
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            Map<String,String> map1 = new HashMap<>();
            map1.put("access_token",socialUser.getAccess_token());
            HttpResponse response1 = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<>(), map1);
            String json1 = EntityUtils.toString(response1.getEntity());
            GiteeUserInfo userInfo = JSON.parseObject(json1, GiteeUserInfo.class);
            socialUser.setUid(userInfo.getId().toString());
            //得知道是哪个社交用户登录的
            //1） 第一次用 微博 进行 社交登录=>注册，进行一对一绑定注册到远程服务的数据库
            //2） 多次用 微博 进行 社交登录=>登录
            //由远程服务来做判断
            R r = memberFeignService.oauthLogin(socialUser);
            if (r.getCode() == 0) {
                MemberResVo memberResVo = r.getData("data", new TypeReference<MemberResVo>(){});
                log.info("社交登录成功，用户信息为：{}" + memberResVo.toString());
                //1 第一次使用SESSION 命令浏览器保存JSESSIONID的cookie
                //以后浏览器访问哪个网站就会带上这个网站的cookie
                //子域之间：gulimall.com auth.guliamll.com member.gulimall.com
                //发卡发的时候(指定域名为父域名)，即使是子系统发的卡，也能让父系统使用
                //TODO 1 默认发的令牌 session=asdfg 作用域是当前域：解决子域session共享问题
                //TODO 2 希望使用json序列化对象到redis中
                //远程登录成功，将远程服务返回的entity放入session中
                session.setAttribute(AuthServerConstant.LOGIN_USER, memberResVo);
//                servletResponse.addCookie(new Cookie("JSESSIONID", "dada").setDomain());
                //登录成功 -> 跳转首页
                return "redirect:http://gulimall.com";
            }else {
                //失败 重新登录
                return "redirect:http://auth.gulimall.com/login.html";
            }

        }else {
            //没有获取了access_token 登录失败 返回到登录页
            return "redirect:http://auth.gulimall.com/login.html";
        }
        }
}
