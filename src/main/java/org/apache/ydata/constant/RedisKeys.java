package org.apache.ydata.constant;

public class RedisKeys {

    //系统配置
    public static String SYSTEM_PROP = "SYSTEM:PROPERTIES";

    //商户信息（系统启动时写入，永不过期）
    public static String MCH_INFO = "MCH:INFO:%s";    //%s=pubkey
    //商户开放平台公钥
    public static String MCH_OPEN_API_KEYS = "MCH:OPENAPI-KEYS:%s";    //%s=pubkey

    //三方基本信息（系统启动时写入，永不过期）
    public static String HUB_INFO = "HUB:INFO:%s";    //%s=userId

    //订单信息
    public static String ORDER_INFO = "ORDER:INFO:%s";    //%s=orderNo
    public static String ORDER_NOTIFY_TIMES = "ORDER:NOTIFY-TIMES:%s";    //%s=orderNo

    //商户订单定向下发
    public static String MERCHANT_PO_DIRECT_TARGET = "MERCHANT:PO:DIRECT-TARGET:%s";    //%s=merchantId, value=targetUserIds

    //充值积分缓存（防表单重复提交）
    public static String RECHARGE_POINTS_CACHE = "ADMIN:RECHARGE-POINTS:%s";    //%s=充值的提交的md5值
    public static String OPENAPI_MEMO_CACHE = "OPENAPI:MEMO:%s";    //%s=远程提交memo设置

    //mq补单并发锁
    public static String ORDER_MQ_SUPPLEMENT = "ORDER:MQ-SUPPLEMENT:%s";    //%s=orderNo

    //系统支付通道
    public static String SYSTEM_PAY_CHANNEL = "SYSTEM:PAY-CHANNEL:%s";  //%s=channelCode, value=Pay Channel

    //三方权重算法相关参数
    //hub被调用次数
    public static String HUB_USE_TIMES = "HUB:USE-TIMES:%s";    //%s=hubId
    //hub连续拉码失败次数
    public static String HUB_FAIL_TIMES = "HUB:FAIL-TIMES:%s";    //%s=hubId

}
