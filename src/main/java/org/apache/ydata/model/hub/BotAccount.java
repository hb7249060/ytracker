package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bot_account")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BotAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; //telegramID

    @Column(columnDefinition = "VARCHAR(64)")
    String username;    //telegram用户名

    @Column(columnDefinition = "VARCHAR(64)")
    String nickname;    //telegram昵称

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

}
