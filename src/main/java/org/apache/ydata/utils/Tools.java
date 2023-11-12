package org.apache.ydata.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Tools {

    /**
     * 返回根据ID生成的钱包地址.
     * 生成规则见源代码
     * @param id
     * @return
     */
    public static String getWalletAddress(String id) throws Exception {
        if(id == null || id.length() < 5) {
            throw new RuntimeException("getWalletAddress error.");
        }
        String sKey = id.substring(0,4) + id.substring(1,5) + id.substring(id.length()-4) + id.substring(id.length()-5, id.length() - 1);
        String aesText = AESTools.encrypt(id, sKey).replace("==", id.substring(id.length() - 2));
        String walletAddress = Base64Util.encode(aesText);
//        log.info(sKey + "," + aesText + "," + walletAddress + "," + walletAddress.length());
        return walletAddress;
    }

    /**
     * 随机生成字符串
     * @param n
     * @return
     */
    public static String getRandomWord(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    /**
     * 生成salt的静态方法
     * @param n
     * @return
     */
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    /**
     * 是否是空的数据列表
     * @param list
     * @return
     */
    public static boolean isEmptyList(List list) {
        if(null == list || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 将时间格式字符串解析为指定的date
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseDateString(String dateStr, String pattern) throws ParseException {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateStr);
    }

    /**
     * 将时间解析为指定的date string
     * @return
     * @throws ParseException
     */
    public static String formatDateString(Date date, String pattern) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将时间解析为指定的date string
     * @return
     * @throws ParseException
     */
    public static String timeDateString(long time, String pattern) throws ParseException {
        if (time == 0) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    /**
     * 生成md5
     * @param message
     * @return
     */
    public static String encryptMD5(String message) {
        String md5str = "";
        try {
            //1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            //2 将消息变成byte数组
            byte[] input = message.getBytes();

            //3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            //4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str.toLowerCase();
    }

    /**
     * 二进制转十六进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if(digital < 0) {
                digital += 256;
            }
            if(digital < 16){
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        //生成默认管理员信息
//        Long id = IdUtil.nextId();
        String username = "admin";
        String salt = getSalt(8);
        String password = getSalt(10);
        String encryptPwd = new Md5Hash(password, salt, 1024).toHex();
        System.out.println(username + ", " + salt + ", " + password + ", " + encryptPwd);

        double money = 1000;
        Random random = new Random();
        for(int i = 0;i<30;i++) {
            int number = random.nextInt(9);
            if(number == 0) number++;
//            System.out.println(number);
            BigDecimal digit = new BigDecimal(number).divide(new BigDecimal(100));
//            if(digit == 1) digit = digit - 0.01;
//            BigDecimal discount = new BigDecimal(digit).setScale(2, BigDecimal.ROUND_UP);
//            System.out.println(new BigDecimal(money).subtract(digit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
//        System.out.println(getIMAccid());

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date startDate = sdf.parse("2020-03-11");
//
//        Date endDate = sdf.parse("2020-03-12");
//        Date now = new Date();
//
//        System.out.println(now.before(endDate));
//        System.out.println(now.after(startDate));

//        final Base64.Decoder decoder = Base64.getDecoder();
//        final Base64.Encoder encoder = Base64.getEncoder();
//        final String text = "字串文字";
//        final byte[] textByte = text.getBytes("UTF-8");
//        //编码
//        final String encodedText = encoder.encodeToString(textByte);
//        System.out.println(encodedText);
//        //解码
//        System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
    }

    /**
     * base64编码
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Encode(String text) throws UnsupportedEncodingException {
        final Base64.Encoder encoder = Base64.getEncoder();
        final byte[] textByte = text.getBytes("UTF-8");
        final String encodedText = encoder.encodeToString(textByte);
        return encodedText;
    }

    /**
     * base64解码
     * @param encodedText
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Decode(String encodedText) throws UnsupportedEncodingException {
        final Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(encodedText), "UTF-8");
    }

    /**
     * 产生四位随机数
     * @return
     */
    public static int randomCode4() {
        return (int)((Math.random()*9+1)*1000);
    }

    /**
     * 产生六位随机数
     * @return
     */
    public static int randomCode6() {
        return (int)((Math.random()*9+1)*100000);
    }

    /**
     * 使用UUID生成唯一订单编号
     * @return
     */
    public static String genOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        //System.out.println(hashCodeV);
        // 0 代表前面补充0
        // 15 代表长度为15
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 使用UUID生成唯一accid
     * @return
     */
    public static String getIMAccid() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        return String.valueOf(hashCodeV);
    }

    public static Response createResponse(int code, String msg, Object data) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

}
