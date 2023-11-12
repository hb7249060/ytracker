package org.apache.ydata.vo.admin;

import lombok.Data;
import org.apache.ydata.vo.SystemSettingsKeys;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class SystemSettingsVo implements Serializable {

    String googleCode;   //google code

    /** 是否开启前端接口加密 **/
    String[] WEB_API_ENCRYPT;
    /** 前端用户登录接口默认加密公、私钥（CurTime+Nonce+PubKey） **/
    String DEFAULT_WEB_API_PRI_KEY;
    String DEFAULT_WEB_API_PUB_KEY;

    /** 商户下单服务器：请求白名单IP **/
    String MERCHANT_WHITE_LIST_IPS;

    /** 商户下单服务器：请求黑名单IP **/
    String MERCHANT_BLOCKED_IPS;

    /** 码商：请求黑名单IP **/
    String USER_BLOCKED_IPS;

    /** 下限费率 **/
    String MIN_FEE_RATE;

    /** 系统状态 **/
    String SYS_STATE;

    /** 派单公平模式 **/
    String PO_FAIR_MODE;

    //钱包功能新增
    /** APP收费是否开启 **/
    String APP_CHARGE_STATE;
    /** APP收费配置 **/
    String APP_CHARGE_CONFIG;
    /** APP收益钱包账户 **/
    String APP_PROFIT_WALLET_ADDRESS;
    /** APP审核配置 **/
    String APP_AUDIT_PROP;

    /** APP是否启用 **/
    String APP_AVALIABLE_STATE;

    /** APP下载地址 **/
    String APP_DOWNLOAD_URL;

    /** 派单保证金 **/
    String ORDER_DISPATCHER_DEPOSIT;

    /** 连续失败自动停码次数 **/
    String AUTO_DISABLE_CODE_RANGE;

    /** 订单金额实付模式，0常规模式，1下浮模式，2上浮模式 **/
    String ORDER_REAL_PAY_MODE;

    /** 收银台页base地址 **/
    String PAY_URL;

    /** 收银台页eip实例 **/
    String PAY_EIP_INSTANCES;

    /** 收银台页ecs实例 **/
    String PAY_ECS_INSTANCES;
    /** 收银台变更订阅方地址 **/
    String PAY_URL_SUBSCRIBES;

    /** 系统收费模式 1预付费，2后付费，默认2**/
    String CHARGE_MODE = "2";

    /** 系统收费费率 **/
    String CHARGE_RATE;

    /** 代付订单功能是否可用 **/
    String PAY_ORDER_ENABLE;

    /** 回调页base地址 **/
    String NOTIFY_URL;

    public Map<String, String> toDataMap() {
        HashMap<String, String > dataMap = new HashMap<>();
        dataMap.put(SystemSettingsKeys.WEB_API_ENCRYPT, (WEB_API_ENCRYPT != null && WEB_API_ENCRYPT.length > 0 && WEB_API_ENCRYPT[0].equals("1")) ? "1" : "0");
        dataMap.put(SystemSettingsKeys.DEFAULT_WEB_API_PRI_KEY, DEFAULT_WEB_API_PRI_KEY);
        dataMap.put(SystemSettingsKeys.DEFAULT_WEB_API_PUB_KEY, DEFAULT_WEB_API_PUB_KEY);
        dataMap.put(SystemSettingsKeys.MERCHANT_WHITE_LIST_IPS, !ObjectUtils.isEmpty(MERCHANT_WHITE_LIST_IPS) ? MERCHANT_WHITE_LIST_IPS.replaceAll(" ", "") : "");
        dataMap.put(SystemSettingsKeys.MERCHANT_BLOCKED_IPS, MERCHANT_BLOCKED_IPS);
        dataMap.put(SystemSettingsKeys.USER_BLOCKED_IPS, USER_BLOCKED_IPS);
        dataMap.put(SystemSettingsKeys.MIN_FEE_RATE, MIN_FEE_RATE);
        dataMap.put(SystemSettingsKeys.SYS_STATE, SYS_STATE);
        dataMap.put(SystemSettingsKeys.PO_FAIR_MODE, PO_FAIR_MODE);
        dataMap.put(SystemSettingsKeys.ORDER_REAL_PAY_MODE, ORDER_REAL_PAY_MODE);
        dataMap.put(SystemSettingsKeys.APP_CHARGE_STATE, APP_CHARGE_STATE);
        dataMap.put(SystemSettingsKeys.APP_CHARGE_CONFIG, APP_CHARGE_CONFIG);
        dataMap.put(SystemSettingsKeys.APP_PROFIT_WALLET_ADDRESS, APP_PROFIT_WALLET_ADDRESS);
        dataMap.put(SystemSettingsKeys.APP_AUDIT_PROP, APP_AUDIT_PROP);
        dataMap.put(SystemSettingsKeys.APP_AVALIABLE_STATE, APP_AVALIABLE_STATE);
        dataMap.put(SystemSettingsKeys.APP_DOWNLOAD_URL, APP_DOWNLOAD_URL);
        dataMap.put(SystemSettingsKeys.ORDER_DISPATCHER_DEPOSIT, ORDER_DISPATCHER_DEPOSIT);
        dataMap.put(SystemSettingsKeys.AUTO_DISABLE_CODE_RANGE, AUTO_DISABLE_CODE_RANGE);
        dataMap.put(SystemSettingsKeys.PAY_URL, !ObjectUtils.isEmpty(PAY_URL) ? PAY_URL.trim() : "");
        dataMap.put(SystemSettingsKeys.PAY_EIP_INSTANCES, !ObjectUtils.isEmpty(PAY_EIP_INSTANCES) ? PAY_EIP_INSTANCES.trim() : "");
        dataMap.put(SystemSettingsKeys.PAY_ECS_INSTANCES, !ObjectUtils.isEmpty(PAY_ECS_INSTANCES) ? PAY_ECS_INSTANCES.trim() : "");
        dataMap.put(SystemSettingsKeys.PAY_URL_SUBSCRIBES, !ObjectUtils.isEmpty(PAY_URL_SUBSCRIBES) ? PAY_URL_SUBSCRIBES.trim() : "");
        dataMap.put(SystemSettingsKeys.CHARGE_MODE, CHARGE_MODE);
        dataMap.put(SystemSettingsKeys.CHARGE_RATE, CHARGE_RATE);
        dataMap.put(SystemSettingsKeys.PAY_ORDER_ENABLE, PAY_ORDER_ENABLE);
        dataMap.put(SystemSettingsKeys.NOTIFY_URL, NOTIFY_URL);
        return dataMap;
    }
}
