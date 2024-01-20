package com.atguigu.gulimall.auth.controller;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.exception.BizCodeEnum;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberResVo;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.feign.ThirdPartyFeignService;
import com.atguigu.gulimall.auth.vo.MemberRegistVo;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegistVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 孟享广
 * @date 2021-01-28 11:34 上午
 * @description
 */
@Controller
public class LoginRegController {
    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone")String phone){
//        String code = UUID.randomUUID().toString().substring(0, 5);

        //验证码60s内
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60*1000) {
                //60s内 不能再次发送
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //TODO 关闭了sendCode 并指定了固定的验证码
        String code = "24673";
        String substring = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,substring,10, TimeUnit.MINUTES);


//        thirdPartyFeignService.sendCode(phone,code);
        return R.ok();
    }



    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes){
        //有问题
        if (result.hasErrors()) {
//            Map<String, String> errors1 = result.getFieldErrors().stream().collect(Collectors.toMap(fieldError -> {
//                return fieldError.getField();//key
//            }, fieldError -> {
//                return fieldError.getDefaultMessage();//value
//            }));
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            //model.addAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("errors", errors);
            //后端校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        String code = vo.getCode();
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());

        if (!StringUtils.isEmpty(redisCode)) {
            //说明redis存了验证码
            if (code.equals(redisCode.split("_")[0])) {
                //说明redis验证码 = 前端传过来的 可以远程注册
                //先删除验证码 令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //远程注册
                MemberRegistVo memberRegistVo = new MemberRegistVo();
                BeanUtils.copyProperties(vo, memberRegistVo);
                R r = memberFeignService.regist(memberRegistVo);
                if (r.getCode() == 0) {
                    //注册成功后回到首页，或者回到登录页
                    return "redirect:http://auth.gulimall.com/login.html";
                }else {
                    //出现异常 或者 失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData("msg", new TypeReference<String>(){}));//R错误消息都在msg里
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }

            }else {
                //说明验证码不对
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码匹配不上");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else {
            //说明验证码没了，过期了
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "redis没有验证码");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session) {

        //远程登录
        R r = memberFeignService.login(vo);
        if (r.getCode() == 0) {
            //远程登录成功，将远程服务返回的entity放入session中
            MemberResVo memberResVo = r.getData("data", new TypeReference<MemberResVo>(){});
            session.setAttribute(AuthServerConstant.LOGIN_USER, memberResVo);
            return "redirect:http://gulimall.com";
        }else {
            //远程登录失败
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", r.getData("msg", new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if(attribute == null){
            return "login";
        } else{
            return "redirect:http://gulimall.com";
        }
    }
//
//    @GetMapping("/reg.html")
//    public String regPage() {
//
//        return "reg";
//    }
    /**
     * ！！！！后来代码优化 见本文的->loginPage()
     * 下面两个空方法仅仅是发送一个请求【直接】跳转一个页面
     * 这样不太好 不要写空方法 去GulimallWebConfig.class
     * 使用 SpringMVC ViewController 将请求和页面映射过来
     */
//    @GetMapping("/login.html")
//    public String loginPage() {
//
//        return "login";
//    }
//
//    @GetMapping("/reg.html")
//    public String regPage() {
//
//        return "reg";
//    }
}
