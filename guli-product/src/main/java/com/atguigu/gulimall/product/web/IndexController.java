package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;


import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = {"/","/index.html"})
    public String indexPage(Model model){
        //1. 查询出所有的一级分类
        List<CategoryEntity> categoryEntityList=categoryService.getLevel1Categories();
        model.addAttribute("categories",categoryEntityList);
        return  "index";
    }

    @GetMapping("index/json/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }


}
