package org.apache.ydata.api.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ydata.utils.MD5;
import org.apache.ydata.utils.SignUtils;
import org.apache.ydata.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Slf4j
@Controller
public class IndexPayTest {

    @Resource
    private HttpServletResponse resp;

    @Resource
    private HttpServletRequest req;

    @Value("${config.unionPay.mch_id}")
    private String mchId;
    @Value("${config.unionPay.key}")
    private String key;
    @Value("${config.unionPay.req_url}")
    private String reqUrl;
    @Value("${config.unionPay.notify_url}")
    private String notifyUrl;

    public static Map<String,String> orderResult; //用来存储订单的交易状态(key:订单号，value:状态(0:未支付，1：已支付))  ---- 这里可以根据需要存储在数据库中
    public static int orderStatus=0;

    @PostMapping(value = "/testPay")
    public void doPayTest() throws IOException {

        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        SortedMap<String,String> map = XmlUtils.getParameterMap(req);

        map.put("mch_id", mchId);
        //重复提交的时候直接查询本地的状态
       /* if(orderResult != null && orderResult.containsKey(map.get("out_trade_no"))){
            String status = "0".equals(orderResult.get(map.get("out_trade_no"))) ? "未支付" : "已支付";
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            resp.getWriter().write(status);
        }else{*/
        map.put("notify_url", notifyUrl);
        map.put("nonce_str", String.valueOf(new Date().getTime()));

        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);

        log.info("reqUrl: {}", reqUrl);
        log.info("reqParams: {}", XmlUtils.parseXML(map));

        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("请求结果01：{}", res);

                if(resultMap.containsKey("sign")){
                    if(!SignUtils.checkParam(resultMap, key)){
                        res = "验证签名不通过";
                        log.info("请求结果02：{}", res);
                    }else{
                        if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                            if(orderResult == null){
                                orderResult = new HashMap<String,String>();
                            }
                            orderResult.put(map.get("out_trade_no"), "0");//初始状态

                            String code_img_url = resultMap.get("code_img_url");
                            //System.out.println("code_img_url"+code_img_url);
                            req.setAttribute("code_img_url", code_img_url);
                            req.setAttribute("out_trade_no", map.get("out_trade_no"));
                            req.setAttribute("total_fee", map.get("total_fee"));
                            req.setAttribute("body", map.get("body"));
                            req.getRequestDispatcher("index-pay-result.jsp").forward(req, resp);
                        }else{
                            req.setAttribute("result", res);
                        }
                    }
                }
            }else{
                res = "操作失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        } finally {
            if(response != null){
                response.close();
            }
            if(client != null){
                client.close();
            }
        }
        if(res.startsWith("<")){
            resp.setHeader("Content-type", "text/xml;charset=UTF-8");
        }else{
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
        }
        resp.getWriter().write(res);
        //}
    }

}
