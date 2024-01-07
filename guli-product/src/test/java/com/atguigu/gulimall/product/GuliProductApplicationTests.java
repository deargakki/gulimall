package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;


@Slf4j
@SpringBootTest
class GuliProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Test
    void testPath(){
        Long[] path = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(path));
    }
//    @Autowired
//    OSSClient ossClient;
    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("HUAWEI");
        brandService.save(brandEntity);
        System.out.println("保存成功");
    }


//    @Test
//    public void testUpload() throws FileNotFoundException{
//        InputStream inputStream = new FileInputStream("C:\\Users\\ga'k'k'i\\OneDrive\\图片\\屏幕快照\\2022-04-27 (1).png");
//
//        ossClient.putObject("gulimall-gakkilove","1.png",inputStream);
//
//        ossClient.shutdown();
//
//        System.out.println("上传完成。。。。");
//    }

}
