package org.apache.ydata.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @program: edu-commom
 * @description: 主键生成
 **/
@Configuration
public class IdUtil {

    @Value("${config.serverWorkerId}")
    private long serverWorkerId;

    @Value("${config.serverDataCenterId}")
    private long serverDataCenterId;

    private TableLongIdUtil tableLongIdUtil = new TableLongIdUtil(serverWorkerId, serverDataCenterId);

    public synchronized long nextId() {
        return tableLongIdUtil.nextId();
    }

    public static void main(String[] args) {
        IdUtil idUtil = new IdUtil();
        System.out.println(idUtil.nextId());
        System.out.println(idUtil.nextId());
        System.out.println(idUtil.nextId());
        System.out.println(idUtil.nextId());
    }
}
