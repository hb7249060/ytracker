package org.apache.ydata.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "admin_log")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdminLog implements Serializable {
    @Id
    private Long id;

    @Column(columnDefinition = "VARCHAR(16) COMMENT 'IP'")
    private String ip;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '事件'")
    String event;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '事件描述'")
    String eventDesc;

    @Column(columnDefinition = "TEXT COMMENT '内容'")
    String content;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

}
