package com.ws.vpn_server.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String remarks;

    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;

}
