package org.apache.ydata.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备类型
 */
public enum DeviceType {

    /** 系统管理员调整积分、后台修改积分 **/
    IOS("ios", "IOS"),
    /** 码商充值 **/
    ANDROID("android", "ANDROID");

    public static List getAllEventType() {
        List list = new ArrayList<>(values().length);
        for (DeviceType enumList : values()) {
            list.add(enumList);
        }
        return list;
    }

    public static List getAllTypeName() {
        List list = new ArrayList<>(values().length);
        for (DeviceType enumList : values()) {
            list.add(enumList.getType());
        }
        return list;
    }

    public static String getDescByType(String type) {
        for (DeviceType enumList : values()) {
            if(type.equals(enumList.getType())) {
                return enumList.desc;
            }
        }
        return null;
    }

    public String getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    DeviceType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;
    private String desc;

}
