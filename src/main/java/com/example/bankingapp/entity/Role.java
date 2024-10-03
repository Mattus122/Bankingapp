package com.example.bankingapp.entity;

public enum Role {
    SUPERADMIN(3),
    BANKADMIN(2),
    USER(1);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

