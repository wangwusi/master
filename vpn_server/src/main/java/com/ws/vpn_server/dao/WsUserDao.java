package com.ws.vpn_server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.vpn_server.domain.entity.WsUserEntity;

import java.util.List;

public interface WsUserDao extends BaseMapper<WsUserEntity> {
    Integer getLastPort();

    List<WsUserEntity> getExpireUserList();
}
