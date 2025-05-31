package com.bookheaven.back.domain;

public enum MemberRole {
    ADMIN("admin"),
    REGULAR("regular");

    private final String roleName;

    MemberRole(String roleName) {
        this.roleName = roleName;
    }

    public String role() {
        return roleName;
    }
}
