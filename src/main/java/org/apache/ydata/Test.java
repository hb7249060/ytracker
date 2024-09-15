package org.apache.ydata;

import com.beust.jcommander.internal.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.vo.SystemSettingsKeys;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Test {

    public static void main(String[] args) {
//        String captionText = "【 AC2024062609322910054569G 】帮忙加急处理，多谢";
//        String captionText = "JL0625214552lewl0tWUE3\n.";
//        String captionText = "JL0625214552lewl0tWUE3 加急";
//        String captionText = "DF2024062521065097504919\n" +
//                "\n" +
//                "备注:  强森";
//        String captionText = "HJZF0624194329cLw1Ygw1Me\n" +
//                "202406243653383968\n" +
//                "通道名称:jh支付中额Uid";
//        String captionText = "JL0625183748VZusueDGrV\n" +
//                "支付途径:古澜支付宝[21925990|8]";
//        String captionText = "订单反馈\n订单ID：JL0625183748VZusueDGrV";
        String captionText = "订单编号: WD5893055864750776870\n" +
                "通道名称: 支付宝中额uid \n" +
                "订单金额: 1000.00 \n" +
                "订单状态: 未付款";

        log.info("get MessagePhoto content3: " + captionText);
        //分析消息内容
        //第一种：直接是订单号
        //第二种：订单号+空格+内容
        //第三种：订单反馈\n订单ID:\n202312140332429757485153
        String filterWords = "订单反馈|订单ID:|【|】|订单号：|[.]";
        if(!ObjectUtils.isEmpty(filterWords)) {
            List<String> filterWordList = Arrays.asList(filterWords.split("[|]"));
            if(!ObjectUtils.isEmpty(filterWordList)) {
                for (String word : filterWordList) {
                    captionText = captionText.replaceAll(word, "");
                }
            }
        }

//        captionText = captionText.replaceAll("\n", "");
        List<String> texts = Lists.newArrayList();
        captionText = captionText.replaceAll("[ ]", "\n");
        String[] captionTexts = captionText.split("\n");
        for(String text : captionTexts) {
            if(isLetterDigit(text)) {
                texts.add(text);
            }
        }
        captionText = !ObjectUtils.isEmpty(texts) ? texts.get(0) : captionText;
        log.info("get MessagePhoto content4: " + captionText);
        if(captionText.contains("系统订单号") && captionText.contains("通道订单号")) {
            captionText = captionText.substring(captionText.indexOf("系统订单号") + "系统订单号".length(),
                    captionText.indexOf("通道订单号")).replace(":", "").replace("：", "").trim();
            log.info("get MessagePhoto content5: " + captionText);
        }
        if(captionText.contains("订单创建时间")) {
            captionText = captionText.substring(0, captionText.indexOf("订单创建时间"));
            log.info("get MessagePhoto content6: " + captionText);
        }
        if(captionText.contains("商户订单号") && captionText.contains("系统订单号")) {
            captionText = captionText.substring(captionText.indexOf("系统订单号") + "系统订单号".length(),
                    captionText.indexOf("订 单  金 额")).replace(":", "").replace("：", "").trim();
            log.info("get MessagePhoto content7: " + captionText);
        }
        if(captionText.contains("订单编号") && captionText.contains("订单金额")) {
            captionText = captionText.substring(captionText.indexOf("订单编号") + "订单编号".length(),
                    captionText.indexOf("订单金额")).replace(":", "").replace("：", "").trim();
            log.info("get MessagePhoto content8: " + captionText);
        }
        if(captionText.contains("系统") && captionText.contains("订单金额")) {
            captionText = captionText.substring(captionText.indexOf("系统") + "系统".length(),
                    captionText.indexOf("订单金额")).replace(":", "").replace("：", "").trim();
            log.info("get MessagePhoto content9: " + captionText);
        }
        if(captionText.contains("单号") && captionText.contains("订单金额")) {
            captionText = captionText.substring(captionText.indexOf("单号") + "单号".length(),
                    captionText.indexOf("订单金额")).replace(":", "").replace("：", "").trim();
            log.info("get MessagePhoto content10: " + captionText);
        }
        if(captionText.contains("支付途径")) {
            captionText = captionText.substring(0, captionText.indexOf("支付途径")).trim();
            log.info("get MessagePhoto content11: " + captionText);
        }
        if(captionText.contains("备注")) {
            captionText = captionText.substring(0, captionText.indexOf("备注")).trim();
            log.info("get MessagePhoto content12: " + captionText);
        }

        captionText = captionText.replace("：", ":").trim();
        if(captionText.contains(":")) {
            String[] txts = captionText.split(":");
            for(String txt : txts) {
                if(isLetterDigit(txt) && txt.length() >= 8) {
                    captionText = txt;
                    break;
                }
            }
        }
        if(isContainChinese(captionText)) {
            log.info("转发查单失败: {}", captionText);
            //回复需要匹配
//            String message = "转发查单失败，订单号解析错误，请联系技术匹配！";
//            replyGroupMessage(update, message);
        } else {
            //进行查单动作
            log.info("进行查单动作:" + captionText);
//            confirmOrder(update, captionText, botAccount);
        }
    }

    /**
     * 校验数字英文
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.trim().matches(regex);
    }

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
