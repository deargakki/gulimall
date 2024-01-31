package com.atguigu.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "9021000134625223";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJWpettkJaeJMDXPY6wp+dBKD1ggwaloChk2JxtBE+eTzIT2MgGZNsEp9QCYGfwonb4DsMxwLX0jbMXrkcLPwKfy+SCLVo5RdUdIkme16goU2I1XnOKdOJcYwLCRkRwlqke6y3+KYwZbSgFmQQugrtd5yADeDfWuBkbDmsHPzdCvYbP8dZV1UZnGLdPKcb5rDQ9u0XhTHYdShyONGWXSGuN7pywaov0jx9o/xsx33zyV3M83q+mQWVqtFJCy6FrTIQ9LbRm7BxSS3F5LQ+uyL6gRzED6nNE4ObIqKQOnXcp1AiyJFlxXzNhtfITmPjwRh3QiPsf7dE4oTalEuxsTWXAgMBAAECggEAatDO4EK6+0yFV7o1qqCsLBnrasHF0+9y/TR2/GHCf0kdYdGLDu2U3hjlWALskWdiemG4ykkCBwDtqKWJY0YAg9FBF1o6aeau6/jsp8aNYnHOsDkDzrZsUZjCBdr/afYxM3USbsepxj1x6IVBMFjeIqpARL2GQWyU0MPxXc31P5gWJXC2fQPuvtbQm7e1wCfDo4Z+57NEqhS/2+WQcQ/JIa5Vm1f2EG9wPhFN189x5zRj2aC39fBg0OCM6GEg9QEc6P9v8iDN4e10SV8wbR+dDz3IWgLP1LNdtGMojoB+iQqZmCYIdEklmLzDtoD+nswNKiexnASavtnBsvvtjL5VgQKBgQD2IUKL0Af2k+c/eUhL8/6tt8QNYOfPiVqyO4MMyvobFSdN4NUdYvYEW+HKqgMKNdd+vBMxf58OwJXnoQ8HOmB9kIdWxZ/eVeQ2EF718xM7i+w6ijs8+kKUh2uzJz89Fct7OA6ZdI1T1RxfPBFf/aLvQJPPBflIzh/ZcvGY1ETi9wKBgQCO3KabZVVgvRKjabL9cDBna6UVbYenIwZcNi41HVw+amni8+JGqF2tc1Vq7S9P2s4kjaxzhqRrYPRBKVYB6aIGhBUL+YkdzzB/KfzCHxeNqtzlFHkmEpTjLxNIVAKe01R9bu66BIUDCZ9rrsM5FT/3NswWvOI8vUe8Jcvngx76YQKBgDm9/c2tJWEcu8CxZFulR1db6Fu3I0sWDzgHLCyBJgHaiZkEqRq2z76gC62vCv2HOiIaX2paZWdhmQl5SHxKnr52fLnHrUTM47gnSQ/sUdPDdM0bBuC8WYhc6NBj4bsj9NPYh9xKcql7MqfJAM6DQ4lbnhGQdjY1XVV3WOSgz/RrAoGALbCkGE7wX2AY9ep4NFYNJQZoZdIB8JJjFlq9rH8XYV7nPo6Bb9epHjAkwW3aE9AG3enxnYESVMVNWQm/WB1AlJTppkrIb6A0AneLPwpynfxnaVyC4opO09nacOdtbx5nhzZTY2rOn4oUUnwyM694JbiAzUTbllFy/9mWvRRSdAECgYEAiPz5Mu9Dvip+YkVJpCdC2vsabYM58syMLj4N4fKHAIYXDhbG9x2P/G242+V4yC9XO8Dh79gB1zSK7+iyW4C2vDROuRDixAQYJwdMc84wIJDGEuwaeVhUq4y4xqKqzO0K919TPc8LzVNX9Mab8JAbb4NPo6hpJL6WN/p+6NBIVYM=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiHLpc9j8wETN+WgI5BGkJuzI8Gq7bpz32lfFTWkSmjRfsgIhSXJ/CJDoJgllygtCSuXLhLpKdZuRAV9dS1oMVfSlNzhUc5hRwbS+yPrAUxx4NE5Lybovl4tbx8ij3W20jLKVn68ATo9/vCzMqNRt2MxAzRqibdphrcbx8qV7KKTB5e990bH3mTVqD+6ybIKRXeVxEWXGUmSNPKNd2XHl+8VGRO2/4i4uy95CK62ChHPc4zagBn13Q5F2BxV1hAeVho7mhpZVhxvqWSGSWLxeFkl5w0E1wmhcKj026CNOIzEBY/g/Ol7S25bXuWC4aU2R0IRjz1G7YwBqQIlD4Yv06wIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息

    //TODO netapp 分配的免费子域名会随机变化
    private  String notify_url="http://3xyz7a.natappfree.cc/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url="http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    private String time_out = "1m";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                +"\"timeout_express\""+time_out+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
