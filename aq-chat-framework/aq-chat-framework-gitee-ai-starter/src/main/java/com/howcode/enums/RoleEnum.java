package com.howcode.enums;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 12:28
 */
public enum RoleEnum {
    USER("user"),
    SYSTEM("system");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
