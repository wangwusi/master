package com.ws.vpn_server.controller;

import com.ws.vpn_server.domain.dto.UserAddDTO;
import com.ws.vpn_server.service.WsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ws/user")
public class WsUserController {

    @Autowired
    private WsUserService wsUserService;

    @PostMapping
    public void addUser(@RequestBody UserAddDTO dto){
        wsUserService.addUser(dto);
    }

}
