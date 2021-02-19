package com.ws.vpn_server.enums;

public enum StatusEnum {

    YES(1, "是"),

    NO(0, "否");

    private Integer code;

    private String remaker;

    StatusEnum(Integer code, String remaker) {
        this.code = code;
        this.remaker = remaker;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRemaker() {
        return remaker;
    }

    public void setRemaker(String remaker) {
        this.remaker = remaker;
    }
}
