package com.ws.vpn_server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.vpn_server.domain.dto.UserAddDTO;
import com.ws.vpn_server.domain.entity.WsUserEntity;

public interface WsUserService extends IService<WsUserEntity> {

    void addUser(UserAddDTO dto);
}
