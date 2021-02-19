package com.ws.vpn_server.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("ws_user")
public class WsUserEntity extends BaseEntity {

    private String username;

    private String password;

    private Date startTime;

    private Date endTime;

    private Integer port;

    private Integer isValid;
}
