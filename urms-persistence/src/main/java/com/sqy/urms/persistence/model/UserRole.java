package com.sqy.urms.persistence.model;

public enum UserRole {

    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    OPERATOR("ROLE_OPERATOR"),
    USER("ROLE_USER");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
