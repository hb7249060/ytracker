package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 三方群组信息
 */
@Data
@Entity
@Table(name = "hub_group")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HubGroup implements Serializable {

    @Transient
    String hubName;
    @Transient
    String groupBotInfo;//组机器人信息

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "BIGINT COMMENT '三方ID'")
    Long hubId;

    @Column(columnDefinition = "BIGINT COMMENT 'telegram群组ID'")
    Long telChatId;

    @Column(columnDefinition = "VARCHAR(64) COMMENT 'telegram群组名称'")
    String telGroupName;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "BIGINT COMMENT '更新时间'")
    Long updated;

    @Column(columnDefinition = "INT COMMENT '状态，1可用，0禁用，-1删除'")
    Integer state;

}
