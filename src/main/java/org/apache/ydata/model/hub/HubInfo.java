package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 三方系统 基本信息
 */
@Data
@Entity
@Table(name = "hub_info")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HubInfo implements Serializable {

    @Transient
    HubData hubData;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "VARCHAR(64) COMMENT '名称'")
    String name;

    @Column(columnDefinition = "TEXT COMMENT '接口地址'")
    String apiUrl;

    @Column(columnDefinition = "DECIMAL(5,5) COMMENT '系统费费率'")
    Double rate;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "BIGINT COMMENT '更新时间'")
    Long updated;

    @Column(columnDefinition = "INT COMMENT '状态，1可用，0禁用，-1删除'")
    Integer state;

}
