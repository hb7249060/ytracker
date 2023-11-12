package org.apache.ydata.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统配置表，白名单IP，黑名单等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_settings")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SystemSettings {

    @Id
    @Column(columnDefinition = "VARCHAR(64) COMMENT '名称'")
    String name;

    @Column(columnDefinition = "TEXT COMMENT '内容'")
    String value;

}
