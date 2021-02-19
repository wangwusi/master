package com.ws.vpn_server.utils;

import cn.hutool.core.util.ObjectUtil;

public class AssertUtil {

    public static void isNotEmpty(Object obj, String massage){

        if(ObjectUtil.isNotEmpty(obj)){
            throw new RuntimeException(massage);
        }
    }

    public static void juage(boolean obj, String massage){

        if(obj){
            throw new RuntimeException(massage);
        }
    }

}
