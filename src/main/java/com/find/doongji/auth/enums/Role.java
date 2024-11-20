package com.find.doongji.auth.enums;

public enum Role {

    ROLE_USER("ROLE_USER", "일반 사용자"),
    ROLE_SELLER("ROLE_SELLER", "일반 판매자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String value;

    Role(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
