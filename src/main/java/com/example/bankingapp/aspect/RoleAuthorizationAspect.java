package com.example.bankingapp.aspect;

import com.example.bankingapp.entity.Role;
import com.example.bankingapp.exception.ForbiddenRequestException;
import com.example.bankingapp.service.JwtService;
import com.example.bankingapp.validation.TokenValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {
    private final HttpServletRequest request;
    private final JwtService jwtService;
    private final TokenValidation tokenValidation;
    @Before("@annotation(roleCheck)")
    public void checkUserRole(JoinPoint joinPoint,RoleCheck roleCheck) throws Exception{
        String token = request.getHeader("Authorization");
        final String hardcodedToken = tokenValidation.removeBearer(token);
        Role userRole = Role.valueOf(jwtService.returnRole(hardcodedToken));
        boolean isAuthorized = false;
        for(Role role : roleCheck.roles()) {
            if(role.equals(userRole)) {
                isAuthorized = true;
                break;
            }
        }
        if (!isAuthorized) {
            throw new ForbiddenRequestException("You do not have the necessary role to access this resource.");
        }
    }
}


