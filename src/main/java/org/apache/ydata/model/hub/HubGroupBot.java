package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 三方群组里的四方机器人信息
 */
@Data
@Entity
@Table(name = "hub_group_bot")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HubGroupBot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "BIGINT COMMENT '三方群组ID'")
    Long groupId;

    @Column(columnDefinition = "VARCHAR(64) COMMENT 'telegram群组ID'")
    String telGroupId;

    @Column(columnDefinition = "VARCHAR(64) COMMENT 'telegram机器人ID'")
    String telBotId;

    @Column(columnDefinition = "VARCHAR(64) COMMENT 'telegram机器人名称'")
    String telBotName;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "BIGINT COMMENT '更新时间'")
    Long updated;

    @Column(columnDefinition = "INT COMMENT '状态，1可用，0禁用，-1删除'")
    Integer state;

}
