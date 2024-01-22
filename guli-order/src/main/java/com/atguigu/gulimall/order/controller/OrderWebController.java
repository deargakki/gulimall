package com.atguigu.gulimall.order.controller;


import com.atguigu.common.exception.NoStockException;
import com.atguigu.common.exception.VerifyPriceException;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderSubmitVo;
import com.atguigu.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();

        model.addAttribute("orderConfirmData",orderConfirmVo);
        return "confirm";
    }


    @PostMapping(value = "/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes attributes) {
        try {
            SubmitOrderResponseVo orderVO = orderService.submitOrder(vo);
            // 创建订单成功，跳转收银台
            model.addAttribute("submitOrderResp", orderVO);// 封装VO订单数据，供页面解析[订单号、应付金额]
            return "pay";
        } catch (Exception e) {
            // 下单失败回到订单结算页
            if (e instanceof VerifyPriceException) {
                String message = ((VerifyPriceException) e).getMessage();
                attributes.addFlashAttribute("msg", "下单失败" + message);
            } else if (e instanceof NoStockException) {
                String message = ((NoStockException) e).getMessage();
                attributes.addFlashAttribute("msg", "下单失败" + message);
            }
            return "redirect:http://order.gulimall.com/toTrade";
        }
    }
}
