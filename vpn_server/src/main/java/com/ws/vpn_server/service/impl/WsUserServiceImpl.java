package com.ws.vpn_server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.vpn_server.enums.StatusEnum;
import com.ws.vpn_server.dao.WsUserDao;
import com.ws.vpn_server.domain.dto.UserAddDTO;
import com.ws.vpn_server.domain.entity.WsUserEntity;
import com.ws.vpn_server.service.WsUserService;
import com.ws.vpn_server.utils.AssertUtil;
import com.ws.vpn_server.utils.SSHClientUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WsUserServiceImpl extends ServiceImpl<WsUserDao, WsUserEntity> implements WsUserService {

    @Value("${ssh.address}")
    private String address;

    @Value("${ssh.username}")
    private String username;

    @Value("${ssh.password}")
    private String password;

    @Override
    public void addUser(UserAddDTO dto) {
        List<WsUserEntity> list = baseMapper.selectList(
                new QueryWrapper<WsUserEntity>().eq("username", dto.getUsername()));

        AssertUtil.juage(list.size() > 1, "系统数据错误，请检查数据！");

        SSHClientUtil sshClient = new SSHClientUtil(address, username, password);

        if(CollUtil.isNotEmpty(list)){
            WsUserEntity userEntity = list.get(0);
            userEntity.setEndTime(dto.getEndTime());

            if(ObjectUtil.equal(userEntity.getIsValid(), StatusEnum.NO.getCode())){
                userEntity.setIsValid(StatusEnum.YES.getCode());
            }

            sshClient.exec("docker container start " + dto.getUsername());

            baseMapper.updateById(userEntity);

            return;
        }

        Integer port = baseMapper.getLastPort();
        port++;

        String str = "docker run -dt --name " + dto.getUsername() +
                " -p " + port + ":" + port +
                " mritd/shadowsocks -s \"-s 0.0.0.0 -p " + port +
                " -m aes-256-cfb -k " + dto.getPassword() +" --fast-open\"";

        System.out.println(str);
        sshClient.exec(str);
        sshClient.exec("firewall-cmd --zone=public --add-port=" + port +"/tcp --permanent");
        sshClient.exec("firewall-cmd --reload");
        sshClient.logout();

        WsUserEntity entity = new WsUserEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setPort(port);

        baseMapper.insert(entity);
    }

    @Override
    public void start() {
        SSHClientUtil sshClient = new SSHClientUtil(address, username, password);
        sshClient.exec("systemctl start  docker.service");

        List<WsUserEntity> list = baseMapper.selectList(new QueryWrapper<>());
        if(CollUtil.isEmpty(list)){
            return;
        }
        list.forEach( m -> {
            sshClient.exec("docker start " + m.getUsername());
        });
        sshClient.logout();
    }
}
