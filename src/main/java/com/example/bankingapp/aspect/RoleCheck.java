package com.example.bankingapp.aspect;

import com.example.bankingapp.entity.Role;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleCheck {
    Role[] roles();
}

