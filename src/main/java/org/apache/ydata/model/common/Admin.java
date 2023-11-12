package org.apache.ydata.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * BOSS管理员
 */
@Data
@Entity
@Table(name = "admin")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Admin implements Serializable {
    @Transient
    String googleCode;   //google code

    @Id
    Long id;

    @Column(columnDefinition = "VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名'")
    String username;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(64) COMMENT '密码'")
    String password;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(8) COMMENT 'salt'")
    String salt;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(32) COMMENT '双因子认证码'")
    String twoFactorCode;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(64) COMMENT '二级密码'")
    String secPwd;

    @JsonIgnore
    @Column(columnDefinition = "VARCHAR(8) COMMENT 'secSalt'")
    String secSalt;

    @Column(columnDefinition = "INT COMMENT '角色'")
    Integer roleId;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "BIGINT COMMENT '更新时间'")
    Long updated;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "INT COMMENT '状态，1可用，0不可用'")
    Integer state;

    public boolean isLocked() {
        return state == 0;
    }
}
