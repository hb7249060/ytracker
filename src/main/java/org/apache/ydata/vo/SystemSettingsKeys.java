package org.apache.ydata.vo;

/**
 * 系统设置项
 */
public interface SystemSettingsKeys {

    /** 是否开启前端接口加密 **/
    String WEB_API_ENCRYPT = "WEB_API_ENCRYPT";
    /** 前端用户登录接口默认加密公、私钥（CurTime+Nonce+PubKey） **/
    String DEFAULT_WEB_API_PRI_KEY = "DEFAULT_WEB_API_PRI_KEY";
    String DEFAULT_WEB_API_PUB_KEY = "DEFAULT_WEB_API_PUB_KEY";

    /** 商户下单服务器：请求白名单IP **/
    String MERCHANT_WHITE_LIST_IPS = "MERCHANT_WHITE_LIST_IPS";

    /** 商户下单服务器：请求黑名单IP **/
    String MERCHANT_BLOCKED_IPS = "MERCHANT_BLOCKED_IPS";

    /** 码商：请求黑名单IP **/
    String USER_BLOCKED_IPS = "USER_BLOCKED_IPS";

    /** 下限费率 **/
    String MIN_FEE_RATE = "MIN_FEE_RATE";

    /** 系统状态 **/
    String SYS_STATE = "SYS_STATE";

    /** 派单公平模式 **/
    String PO_FAIR_MODE = "PO_FAIR_MODE";

    //钱包功能新增
    /** APP收费是否开启 **/
    String APP_CHARGE_STATE = "APP_CHARGE_STATE";
    /** APP收费配置 **/
    String APP_CHARGE_CONFIG = "APP_CHARGE_CONFIG";
    /** APP收益钱包账户 **/
    String APP_PROFIT_WALLET_ADDRESS = "APP_PROFIT_WALLET_ADDRESS";
    /** APP审核配置 **/
    String APP_AUDIT_PROP = "APP_AUDIT_PROP";

    /** APP是否启用 **/
    String APP_AVALIABLE_STATE = "APP_AVALIABLE_STATE";

    /** APP下载地址 **/
    String APP_DOWNLOAD_URL = "APP_DOWNLOAD_URL";

    /** 派单保证金 **/
    String ORDER_DISPATCHER_DEPOSIT = "ORDER_DISPATCHER_DEPOSIT";

    /** 连续失败自动停码次数 **/
    String AUTO_DISABLE_CODE_RANGE = "AUTO_DISABLE_CODE_RANGE";

    /** 订单金额实付模式，0常规模式，1下浮模式，2上浮模式 **/
    String ORDER_REAL_PAY_MODE = "ORDER_REAL_PAY_MODE";

    /** 收银台页base地址 **/
    String PAY_URL = "PAY_URL";

    /** 收银台页eip实例 **/
    String PAY_EIP_INSTANCES = "PAY_EIP_INSTANCES";

    /** 收银台页ecs实例 **/
    String PAY_ECS_INSTANCES = "PAY_ECS_INSTANCES";

    /** 收银台变更订阅方地址 **/
    String PAY_URL_SUBSCRIBES = "PAY_URL_SUBSCRIBES";

    /** 系统收费模式 **/
    String CHARGE_MODE = "CHARGE_MODE";

    /** 系统收费费率 **/
    String CHARGE_RATE = "CHARGE_RATE";

    /** 代付订单功能是否可用 **/
    String PAY_ORDER_ENABLE = "PAY_ORDER_ENABLE";

    /** 回调页base地址 **/
    String NOTIFY_URL = "NOTIFY_URL";
}
