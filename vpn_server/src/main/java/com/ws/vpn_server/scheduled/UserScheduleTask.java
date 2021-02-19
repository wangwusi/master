package com.ws.vpn_server.scheduled;

import cn.hutool.core.collection.CollUtil;
import com.ws.vpn_server.dao.WsUserDao;
import com.ws.vpn_server.domain.entity.WsUserEntity;
import com.ws.vpn_server.enums.StatusEnum;
import com.ws.vpn_server.utils.SSHClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class UserScheduleTask {

    private final WsUserDao userDao;

    @Value("${ssh.address}")
    private String address;

    @Value("${ssh.username}")
    private String username;

    @Value("${ssh.password}")
    private String password;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "* * 0 * * ?")
    public void configureTasks() {
        List<WsUserEntity> list = userDao.getExpireUserList();
        if(CollUtil.isEmpty(list)){
            return;
        }

        SSHClientUtil sshClient = new SSHClientUtil(address, username, password);

        list.forEach( m -> {
            sshClient.exec("docker stop " + m.getUsername());
            System.out.println("执行结束：" + m.getUsername());

            m.setIsValid(StatusEnum.NO.getCode());
            userDao.updateById(m);
        });

        sshClient.logout();
    }

}
