package com.example.bankingapp.validation;

import com.example.bankingapp.entity.Role;
import com.example.bankingapp.exception.ForbiddenRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserAndAccountRoleValidation {
    public boolean checkIfSameUser(Role roleFromToken,String emailFromToken, String userEmail){
        if(roleFromToken.equals(Role.USER)){
            if(emailFromToken.equals(userEmail)){return true;}
            else{throw new ForbiddenRequestException("Cannot access Other Users Data");}
        }
        return true;
    }
}
